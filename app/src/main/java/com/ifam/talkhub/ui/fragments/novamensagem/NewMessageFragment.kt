package com.ifam.talkhub.ui.fragments.novamensagem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ifam.talkhub.data.model.User
import com.ifam.talkhub.databinding.NovaMensagemBinding
import com.ifam.talkhub.ui.alertdialog.LoadingAlertDialog
import com.ifam.talkhub.ui.fragments.novamensagem.adapter.NewMessageAdapter
import com.ifam.talkhub.ui.fragments.novamensagem.adapter.OnUserClickListener
import com.ifam.talkhub.ui.presenter.FirebaseRetrieveFromFireStoreNewMessagePresenter
import com.ifam.talkhub.ui.view.NovaMensagemView
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import javax.inject.Inject


/**
 * Fragmento responsável por exibir a lista de usuários para iniciar uma nova conversa.
 */
@AndroidEntryPoint
class NewMessageFragment : Fragment(), FirebaseRetrieveFromFireStoreNewMessagePresenter,
    OnUserClickListener {
    private lateinit var binding: NovaMensagemBinding
    private val novaMensagemView: NovaMensagemView by viewModels()
    @Inject
    lateinit var newMessageAdapter: NewMessageAdapter
    private lateinit var loadingAlertDialog: LoadingAlertDialog
    private val currentUser = Firebase.auth.currentUser


    /**
     * Infla o layout da tela de novas mensagens.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NovaMensagemBinding.inflate(inflater, container, false)

        // Configura o presenter para esta classe.
        novaMensagemView.repo.firebaseRetrieveFromFireStore.firebaseRetrieveFromFireStoreNewMessagePresenter =
            this

        // Inicializa o alert dialog de carregamento.
        loadingAlertDialog = LoadingAlertDialog(requireContext())

        // Obtém a lista de todos os usuários.
        novaMensagemView.getAllUser()
        loadingAlertDialog.showLoadingAlertDialog()
        return binding.root
    }

    /**
     * Configura os cliques de botões e lida com a inicialização da tela.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o RecyclerView para exibir a lista de usuários.
        setUpRecyclerView()

        // Observa a lista de usuários e atualiza o adaptador quando ela é alterada.
        novaMensagemView.userLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                newMessageAdapter.setData(it)
            }
            loadingAlertDialog.hideLoadingAlertDialog()
        }

        // Observa a lista de usuários e atualiza o adaptador quando ela é alterada.
        binding.botaoVoltarImagebuttom.setOnClickListener {
            goToLatestMessagesFragment()
        }

        // Configura o SearchView para buscar usuários conforme o texto digitado.
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    novaMensagemView.searchOnUsers(query)

                    novaMensagemView.searchUsersLiveData.observe(viewLifecycleOwner) {
                        if (it != null) {
                            newMessageAdapter.setData(it as ArrayList<User>)
                        }
                    }
                } else {
                    novaMensagemView.getAllUser()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    novaMensagemView.searchOnUsers(newText)

                    novaMensagemView.searchUsersLiveData.observe(viewLifecycleOwner) {
                        if (it != null) {
                            newMessageAdapter.setData(it as ArrayList<User>)
                        }
                    }
                } else {
                    novaMensagemView.getAllUser()
                }
                return true
            }
        })

    }

    /**
     * Configura o RecyclerView para exibir a lista de usuários.
     */
    private fun setUpRecyclerView() {
        binding.usuariosRecView.adapter = newMessageAdapter
        newMessageAdapter.onClickListener = this
        binding.usuariosRecView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.usuariosRecView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 200
        }
    }

    /**
     * Navega para o fragmento de mensagens mais recentes.
     */
    private fun goToLatestMessagesFragment() {
        val action = NewMessageFragmentDirections.actionNewMessageFragmentToLatestMessagesFragment()
        findNavController().navigate(action)
    }

    /**
     * Navega para o fragmento de mensagens mais recentes.
     */
    private fun goToChatFragment(user: User) {
        val action = NewMessageFragmentDirections.actionNewMessageFragmentToChatFragment(user)
        findNavController().navigate(action)
    }

    /**
     * Trata o resultado da recuperação de dados do Firebase Firestore.
     */
    override fun ifRetrieveFromFirebaseSuccess(isSuccess: Boolean, state: String) {
        if (!isSuccess) {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
            loadingAlertDialog.hideLoadingAlertDialog()
        }
    }

    /**
     * Manipula o clique em um usuário na lista.
     */
    override fun onUserClickListener(user: User) {
        if (user.email == currentUser!!.email) {
            Toast.makeText(requireContext(), "Você", Toast.LENGTH_SHORT).show()
        } else {
            goToChatFragment(user)
        }
    }

}