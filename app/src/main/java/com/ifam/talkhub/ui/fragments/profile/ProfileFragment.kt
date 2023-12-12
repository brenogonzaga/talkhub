package com.ifam.talkhub.ui.fragments.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.ifam.talkhub.R
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.data.model.User
import com.ifam.talkhub.databinding.TelaPerfilBinding
import com.ifam.talkhub.ui.alertdialog.LoadingAlertDialog
import com.ifam.talkhub.ui.presenter.FirebaseFireStoreSaveDataProfilePresenter
import com.ifam.talkhub.ui.presenter.FirebaseStorageProfilePresenter
import com.ifam.talkhub.ui.view.PerfilView
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment responsável por exibir e permitir a edição do perfil do usuário.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment(), FirebaseStorageProfilePresenter,
    FirebaseFireStoreSaveDataProfilePresenter {
    // Binding da tela de perfil
    private lateinit var binding: TelaPerfilBinding

    // ViewModel associada à tela de perfil
    private val perfilView: PerfilView by viewModels()

    // Instância do LoadingAlertDialog para exibir e ocultar o diálogo de carregamento
    private lateinit var loadingAlertDialog: LoadingAlertDialog

    // Usuário atualmente logado
    private var currentUser: User? = null

    // Uri da imagem selecionada pelo usuário
    private var selectedUri: Uri? = null

    /**
     * Método chamado para criar a view associada ao fragmento.
     *
     * @param inflater O LayoutInflater responsável por inflar o layout do fragmento.
     * @param container O ViewGroup no qual a view do fragmento será inserida.
     * @param savedInstanceState Dados opcionais para recriar o fragmento a partir de um estado salvo anteriormente.
     * @return A view associada ao fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TelaPerfilBinding.inflate(inflater, container, false)

        // Inicializa o LoadingAlertDialog
        loadingAlertDialog = LoadingAlertDialog(requireContext())

        // Associa os callbacks para os presenters
        perfilView.repo.firebaseStorageProfile.firebaseStorageProfilePresenter = this
        perfilView.repo.firebaseFireStoreSaveData.firebaseFireStoreSaveDataProfilePresenter = this
        // Obtém o usuário atualmente logado
        perfilView.getCurrentUser()

        // Exibe o diálogo de carregamento
        loadingAlertDialog.showLoadingAlertDialog()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa as mudanças no LiveData do usuário atual
        perfilView.currentUserLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                currentUser = it
                setUserDataInUi()
            }
            // Oculta o diálogo de carregamento
            loadingAlertDialog.hideLoadingAlertDialog()
        }

        // Define o clique na imagem de perfil para abrir a galeria
        binding.imagemUsuarioCirimgview.setOnClickListener {
            openPhotoGalleryToSelectImage()
        }

        // Define o clique no botão de salvar para atualizar os dados do perfil
        binding.salvarButton.setOnClickListener {
            if (!binding.edUsername.text.isNullOrEmpty()) {
                // Exibe o diálogo de carregamento
                loadingAlertDialog.showLoadingAlertDialog()
                if (selectedUri != null) {
                    // Se uma nova imagem foi selecionada, realiza o upload
                    perfilView.uploadPhotoToFirebaseStorageProfile(selectedUri!!)
                } else {
                    // Se não, atualiza apenas os dados do usuário
                    updateUser(null, null)
                }
            } else {
                binding.edUsername.error = Constants.REQUIRED_FIELD
            }
        }

        // Define o clique longo na imagem de perfil para confirmar a exclusão da imagem
        binding.imagemUsuarioCirimgview.setOnLongClickListener {
            confirmDeleteProfileImage()
            true
        }

        // Define o clique no botão de voltar para navegar de volta para as últimas mensagens
        binding.botaoVoltarImagebuttom.setOnClickListener {
            goToLatestMessagesFragment()
        }
    }

    /**
     * Atualiza a interface do usuário com os dados do usuário atual.
     */
    private fun setUserDataInUi() {
        if (currentUser?.perfilImgUrl != null) {
            binding.imagemUsuarioCirimgview.load(currentUser?.perfilImgUrl)
        } else {
            binding.imagemUsuarioCirimgview.setImageResource(R.drawable.usuario_sem_perfil)
        }
        binding.mEmailTextview.text = currentUser?.email
        binding.edUsername.setText(currentUser?.username)
    }

    /**
     * Navega para o fragmento de últimas mensagens.
     */
    private fun goToLatestMessagesFragment() {
        val action = ProfileFragmentDirections.actionProfileFragmentToLatestMessagesFragment()
        findNavController().navigate(action)
    }

    /**
     * Abre a galeria de fotos para permitir ao usuário selecionar uma imagem.
     */
    private fun openPhotoGalleryToSelectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncherOfSelectPhoto.launch(intent)
    }

    /**
     * Resultado da seleção de uma foto da galeria.
     */
    private var resultLauncherOfSelectPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent: Intent? = result.data
                val uri = intent!!.data

                // Carrega a imagem selecionada na ImageView
                binding.imagemUsuarioCirimgview.load(uri)
                // Salva a Uri da imagem selecionada
                selectedUri = uri

            } else {
                Toast.makeText(requireContext(), "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
            }
        }

    /**
     * Atualiza os dados do usuário no Firebase Firestore.
     *
     * @param uri A Uri da nova imagem de perfil (se houver).
     * @param imageId O ID da imagem no Firebase Storage (se houver).
     */
    private fun updateUser(uri: Uri?, imageId: String?) {
        if (uri != null && imageId != null) {
            // Se uma nova imagem foi fornecida, cria um novo usuário com a imagem
            val id = currentUser!!.id
            val username = binding.edUsername.text.toString()
            val email = currentUser!!.email
            val profileImage = uri.toString()
            val user = User(id, username, email, profileImage, imageId)
            // Atualiza o usuário no Firestore
            perfilView.updateUserInFireStoreDB(user)
        } else {
            // Se não, atualiza apenas o nome de usuário
            val id = currentUser!!.id
            val username = binding.edUsername.text.toString()
            val email = currentUser!!.email
            val profileImage = currentUser!!.perfilImgUrl
            val profileImageId = currentUser!!.perfilImgId
            val user = User(id, username, email, profileImage, profileImageId)
            // Atualiza o usuário no Firestore
            perfilView.updateUserInFireStoreDB(user)
        }
    }

    /**
     * Exibe um diálogo de confirmação antes de excluir a imagem de perfil.
     */
    private fun confirmDeleteProfileImage() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Excluir imagem de perfil")
        builder.setMessage("Tem certeza de que deseja excluir sua imagem de perfil?")
        builder.setPositiveButton("Sim") { _, _ ->
            // Confirma a exclusão da imagem de perfil
            deleteProfileImage()
        }
        builder.setNegativeButton("Não") { _, _ -> }
        builder.show()
    }

    /**
     * Exclui a imagem de perfil atual.
     */
    private fun deleteProfileImage() {
        if (currentUser!!.perfilImgUrl != null) {
            // Se houver uma imagem de perfil, exclui a imagem do Firebase Storage
            loadingAlertDialog.showLoadingAlertDialog()
            perfilView.deleteImageFromFireStorageProfile(currentUser!!.perfilImgId!!)
            // Atualiza o usuário no Firestore sem a imagem
            perfilView.updateUserInFireStoreDB(
                user = User(
                    currentUser!!.id,
                    currentUser!!.username,
                    currentUser!!.email,
                    null,
                    null
                )
            )
            // Define a imagem de perfil padrão
            binding.imagemUsuarioCirimgview.setImageResource(R.drawable.usuario_sem_perfil)
        } else {
            // Se não houver imagem de perfil, exibe uma mensagem informando o usuário
            Toast.makeText(requireContext(), "Você não tem imagem de perfil", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * Callback chamado após o upload bem-sucedido de uma nova foto de perfil para o Firebase Storage.
     *
     * @param isSuccess Indica se o upload foi bem-sucedido.
     * @param statue Mensagem de status do upload.
     * @param uri A Uri da imagem de perfil (se houver).
     * @param imageId O ID da imagem no Firebase Storage (se houver).
     */
    override fun isUploadPhotoSuccessful(
        isSuccess: Boolean,
        statue: String,
        uri: Uri?,
        imageId: String?
    ) {
        if (isSuccess) {
            // Se o upload foi bem-sucedido, verifica se há uma imagem de perfil anterior para excluir
            if (currentUser?.perfilImgUrl != null) {
                perfilView.deleteImageFromFireStorageProfile(currentUser!!.perfilImgId!!)
            }
            // Atualiza os dados do usuário no Firestore
            updateUser(uri!!, imageId)
        } else {
            // Se o upload falhou, exibe uma mensagem de erro
            Toast.makeText(requireContext(), statue, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Callback chamado após a atualização bem-sucedida dos dados do usuário no Firebase Firestore.
     *
     * @param isSuccess Indica se a atualização foi bem-sucedida.
     * @param state Mensagem de status da atualização.
     */
    override fun isUpdateUserFromFireStoreSuccess(isSuccess: Boolean, state: String) {
        // Oculta o diálogo de carregamento
        loadingAlertDialog.hideLoadingAlertDialog()
        if (isSuccess) {
            // Se a atualização foi bem-sucedida, exibe uma mensagem de sucesso
            Toast.makeText(requireContext(), "Atualizado", Toast.LENGTH_SHORT).show()
        } else {
            // Se a atualização falhou, exibe uma mensagem de erro
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
        }
        // Atualiza os dados do usuário na view model
        perfilView.getCurrentUser()
    }

}