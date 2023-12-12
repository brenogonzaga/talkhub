package com.ifam.talkhub.ui.fragments.ultimasmensagens

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ifam.talkhub.R
import com.ifam.talkhub.data.model.UserUltimaMensagem
import com.ifam.talkhub.data.model.User
import com.ifam.talkhub.databinding.TelaUltimasMensagensBinding
import com.ifam.talkhub.ui.fragments.ultimasmensagens.adapter.LatestUserAndMessagesAdapter
import com.ifam.talkhub.ui.fragments.ultimasmensagens.adapter.OnLatestUserClickListener
import com.ifam.talkhub.ui.view.UltimasMensagensView
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import javax.inject.Inject

/**
 * Fragment responsável por exibir as últimas mensagens e interações com outros usuários.
 *
 * Esta classe gerencia a exibição das últimas mensagens, permite buscar usuários e mensagens,
 * e oferece opções de navegação para outros destinos, como a criação de novas mensagens e o perfil do usuário.
 *
 * @property binding Instância da classe de vinculação da interface do usuário.
 * @property ultimasMensagensView ViewModel que contém a lógica de negócios relacionada às últimas mensagens.
 * @property latestUserAndMessagesAdapter Adaptador para exibir a lista de usuários e suas últimas mensagens.
 * @property auth Instância do Firebase Authentication para autenticação do usuário.
 * @property currentUser Usuário atualmente autenticado.
 */
@AndroidEntryPoint
class LatestMessagesFragment : Fragment(), OnLatestUserClickListener {
    private lateinit var binding: TelaUltimasMensagensBinding
    private val ultimasMensagensView: UltimasMensagensView by viewModels()
    @Inject
    lateinit var latestUserAndMessagesAdapter: LatestUserAndMessagesAdapter
    @Inject
    lateinit var auth: FirebaseAuth
    private val currentUser: FirebaseUser? = Firebase.auth.currentUser

    /**
     * Inicializa o fragmento, obtendo dados do usuário atual e as últimas mensagens.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TelaUltimasMensagensBinding.inflate(inflater, container, false)

        ultimasMensagensView.getCurrentUser()
        ultimasMensagensView.getLatestUserAndMessages(currentUser!!.uid)

        return binding.root
    }

    /**
     * Configura as ações do fragmento ao ser criado, como configurar o RecyclerView, lidar com o botão de retorno e
     * definir comportamentos para cliques em itens do RecyclerView, imagem de perfil e itens do menu de opções.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        binding.imagemUsuarioCircleimageview.setOnClickListener {
            goToProfileFragment()
        }

        ultimasMensagensView.currentUserProfileImageLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.imagemUsuarioCircleimageview.load(it)
            }
        }

        ultimasMensagensView.latestUserAndMessagesLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                latestUserAndMessagesAdapter.setData(it)
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.new_message_menu -> {
                    goToNewMessageFragment()
                }
                R.id.logout_menu -> {
                    auth.signOut()
                    Toast.makeText(requireContext(), "Sign out", Toast.LENGTH_SHORT).show()
                    goToLoginFragment()
                }
            }
            true
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    ultimasMensagensView.searchOnLatestUserAndMessages(currentUser!!.uid, query)

                    ultimasMensagensView.searchOnUsersLiveData.observe(viewLifecycleOwner) {
                        if (it != null) {
                            latestUserAndMessagesAdapter.setData(it)
                        }
                    }
                } else {
                    ultimasMensagensView.getLatestUserAndMessages(currentUser!!.uid)
                }
                return true
            }

            /**
             * Método chamado quando o texto da consulta na barra de pesquisa é alterado. Realiza uma busca nos usuários e mensagens mais recentes
             * com base no texto da consulta.
             *
             * @param newText Novo texto da consulta.
             * @return true para indicar que a ação foi tratada.
             */
            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    // Realiza uma busca nos usuários e mensagens mais recentes com base no texto da consulta.
                    ultimasMensagensView.searchOnLatestUserAndMessages(currentUser!!.uid, newText)
                    // Observa as alterações nos resultados da busca e atualiza o adaptador conforme necessário.
                    ultimasMensagensView.searchOnUsersLiveData.observe(viewLifecycleOwner) {
                        if (it != null) {
                            latestUserAndMessagesAdapter.setData(it)
                        }
                    }
                } else {
                    ultimasMensagensView.getLatestUserAndMessages(currentUser!!.uid)
                }
                return true
            }
        })

    }

    /**
     * Configura o RecyclerView e seus componentes relacionados para exibir a lista de usuários e suas últimas mensagens.
     */
    private fun setupRecyclerView() {
        binding.latestUsersRecView.adapter = latestUserAndMessagesAdapter
        latestUserAndMessagesAdapter.onUserClickListener = this
        binding.latestUsersRecView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.latestUsersRecView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 200
        }
    }

    /**
     * Manipula a navegação quando o fragmento é iniciado, direcionando o usuário para a tela de login caso não esteja autenticado.
     */
    override fun onStart() {
        super.onStart()

        if (currentUser == null) {
            goToLoginFragment()
        }
    }

    /**
     * Navega para o fragmento de nova mensagem ao ser acionado o item de menu correspondente.
     */
    private fun goToNewMessageFragment() {
        val action =
            LatestMessagesFragmentDirections.actionLatestMessagesFragmentToNewMessageFragment()
        findNavController().navigate(action)
    }

    /**
     * Navega para o fragmento de login.
     */
    private fun goToLoginFragment() {
        val action = LatestMessagesFragmentDirections.actionLatestMessagesFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    /**
     * Navega para o fragmento de chat com o usuário especificado.
     *
     * @param user para navegar para o fragmento do respectivo chat.
     */
    private fun goToChatFragment(user: User) {
        val action =
            LatestMessagesFragmentDirections.actionLatestMessagesFragmentToChatFragment(user)
        findNavController().navigate(action)
    }

    /**
     * Navega para o fragmento de perfil do usuário.
     */
    private fun goToProfileFragment() {
        val action =
            LatestMessagesFragmentDirections.actionLatestMessagesFragmentToProfileFragment()
        findNavController().navigate(action)
    }

    /**
     * Manipula o clique em um item da lista de usuários, direcionando o usuário para o fragmento de chat correspondente.
     *
     * @param userUltimaMensagem Objeto contendo informações sobre o usuário e sua última mensagem.
     */
    override fun onLatestUserClickListener(userUltimaMensagem: UserUltimaMensagem) {
        goToChatFragment(userUltimaMensagem.user)
    }

}