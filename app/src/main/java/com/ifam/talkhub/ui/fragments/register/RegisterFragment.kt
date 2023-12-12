package com.ifam.talkhub.ui.fragments.register

import android.app.Activity
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
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.databinding.TelaRegistroBinding
import com.ifam.talkhub.ui.alertdialog.LoadingAlertDialog
import com.ifam.talkhub.ui.presenter.FirebaseAuthRegisterPresenter
import com.ifam.talkhub.ui.presenter.FirebaseFireStoreRegisterPresenter
import com.ifam.talkhub.ui.presenter.FirebaseStorageRegisterPresenter
import com.ifam.talkhub.ui.view.RegistroView
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment responsável por gerenciar o processo de registro de um novo usuário.
 *
 * Essa classe implementa as interfaces necessárias para lidar com as operações de registro no Firebase,
 * incluindo a autenticação, o armazenamento de arquivos (como imagens de perfil) e o armazenamento de dados
 * no Firestore. Utiliza a anotação @AndroidEntryPoint para permitir a injeção de dependência com Hilt.
 *
 * @property binding Instância da classe de vinculação da interface do usuário.
 * @property registroView ViewModel que contém a lógica de negócios relacionada ao registro.
 * @property loadingAlertDialog Diálogo de carregamento exibido durante operações assíncronas.
 * @property selectedUri URI da imagem de perfil selecionada pelo usuário.
 * @property currentEmail E-mail do usuário atualmente em processo de registro.
 * @property currentUsername Nome de usuário do usuário atualmente em processo de registro.
 */
@AndroidEntryPoint
class RegisterFragment : Fragment(),
    FirebaseAuthRegisterPresenter,
    FirebaseStorageRegisterPresenter,
    FirebaseFireStoreRegisterPresenter {
    private lateinit var binding: TelaRegistroBinding
    private val registroView: RegistroView by viewModels()
    private lateinit var loadingAlertDialog: LoadingAlertDialog
    private var selectedUri: Uri? = null
    private var currentEmail: String? = null
    private var currentUsername: String? = null

    // Método chamado quando a view do fragmento é criada
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TelaRegistroBinding.inflate(inflater, container, false)

        // Configura os presenters do ViewModel
        registroView.repo.firebaseAuthentication.firebaseAuthRegisterPresenter = this
        registroView.repo.firebaseStorageSaveDataRegister.firebaseStorageRegisterPresenter = this
        registroView.repo.firebaseFireStoreSaveData.firebaseFireStoreRegisterPresenter = this

        loadingAlertDialog = LoadingAlertDialog(requireContext())

        return binding.root
    }

    // Método chamado após a criação da view
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o clique na imagem para abrir a galeria
        binding.crcImgUser.setOnClickListener {
            openPhotoGalleryToSelectImage()
        }

        // Configura o clique no texto para ir para a tela de login
        binding.tvUserHaveAlreadyAccount.setOnClickListener {
            goToLoginFragment()
        }

        // Configura o clique no botão de registro
        binding.btnRegister.setOnClickListener {
            val username = binding.edUsername.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edSenha.text.toString()
            val verifyPassword = binding.edVerifyPassword.text.toString()

            val validation = verifyDataFormUser(username, email, password, verifyPassword)
            val matchPasswords = verifyMatchPasswords(password, verifyPassword)

            if (validation && matchPasswords) {
                currentEmail = email
                currentUsername = username
                registroView.createNewUserAccount(email, password)
                loadingAlertDialog.showLoadingAlertDialog()
            }
        }
    }

    // Método para verificar os dados fornecidos pelo usuário no formulário
    private fun verifyDataFormUser(
        username: String,
        email: String,
        password: String,
        verifyPassword: String
    ): Boolean {

        if (username.isEmpty() && username.isBlank()) {
            binding.edUsername.error = Constants.REQUIRED_FIELD
        }
        if (email.isEmpty() && email.isBlank()) {
            binding.edEmail.error = Constants.REQUIRED_FIELD
        }
        if (password.isEmpty() && password.isBlank()) {
            binding.edSenha.error = Constants.REQUIRED_FIELD
        }
        if (verifyPassword.isEmpty() && verifyPassword.isBlank()) {
            binding.edVerifyPassword.error = Constants.REQUIRED_FIELD
        }

        return (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && verifyPassword.isNotEmpty())
    }

    // Método para verificar se as senhas fornecidas coincidem
    private fun verifyMatchPasswords(password: String, verifyPassword: String): Boolean {
        return if (password == verifyPassword) {
            true
        } else {
            Toast.makeText(requireContext(), "As senhas não coincidem", Toast.LENGTH_SHORT).show()
            false
        }
    }

    // Método para abrir a galeria de fotos
    private fun openPhotoGalleryToSelectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncherOfSelectPhoto.launch(intent)
    }

    // Result launcher para receber o resultado da seleção de fotos
    private var resultLauncherOfSelectPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent: Intent? = result.data
                val uri = intent!!.data

                // Carrega a imagem selecionada na view
                binding.crcImgUser.load(uri)
                selectedUri = uri

            } else {
                Toast.makeText(requireContext(), "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
            }
        }

    // Método para ir para a tela de login
    private fun goToLoginFragment() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    // Métodos da interface FirebaseAuthRegisterPresenter
    override fun ifCreateNewUserAccountSuccess(ifSuccess: Boolean, state: String) {
        if (ifSuccess) {
            // Se o registro foi bem-sucedido, envia a verificação por e-mail
            registroView.sendEmailVerificationRegister()
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
            loadingAlertDialog.hideLoadingAlertDialog()
        }
    }

    // Métodos da interface FirebaseStorageRegisterPresenter
    override fun ifSendEmailVerificationSuccessRegister(ifSuccess: Boolean, state: String) {
        if (ifSuccess) {
            // Se o envio de verificação por e-mail foi bem-sucedido, faz o upload da foto (se houver)
            if (selectedUri != null) {
                registroView.uploadPhotoToFirebaseStorage(selectedUri!!)
            } else {
                // Se não houver foto, insere o usuário no Firestore sem a imagem
                registroView.insertUserToFireStoreDB(
                    currentUsername!!,
                    currentEmail!!,
                    null,
                    null
                )
            }
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
            loadingAlertDialog.hideLoadingAlertDialog()
        }
    }

    // Métodos da interface FirebaseStorageRegisterPresenter
    override fun ifImageUploadedSuccess(ifSuccess: Boolean, state: String, uri: Uri?, imageId: String?) {
        if (ifSuccess) {
            // Se o upload da imagem foi bem-sucedido, insere o usuário no Firestore com a imagem
            registroView.insertUserToFireStoreDB(
                currentUsername!!,
                currentEmail!!,
                uri.toString(),
                imageId
            )
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
            loadingAlertDialog.hideLoadingAlertDialog()
        }
    }

    // Métodos da interface FirebaseFireStoreRegisterPresenter
    override fun ifUserInsertedSuccess(ifSuccess: Boolean, state: String) {
        loadingAlertDialog.hideLoadingAlertDialog()
        if (ifSuccess) {
            Toast.makeText(requireContext(), "Sucesso, verifique sua conta e faça login", Toast.LENGTH_SHORT)
                .show()
            goToLoginFragment()
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
        }
    }
}
