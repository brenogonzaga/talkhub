package com.ifam.talkhub.ui.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ifam.talkhub.data.model.User
import com.ifam.talkhub.databinding.TelaChatBinding
import com.ifam.talkhub.ui.fragments.chat.adapter.MensagemAdapter
import com.ifam.talkhub.ui.presenter.FirebaseRealTimeChatPresenter
import com.ifam.talkhub.ui.view.ChatView
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

/**
 * Fragmento responsável por exibir a tela de chat entre dois usuários.
 *
 * @constructor Cria uma instância de [ChatFragment].
 */
@AndroidEntryPoint
class ChatFragment : Fragment(), FirebaseRealTimeChatPresenter {
    private lateinit var binding: TelaChatBinding
    private val args by navArgs<ChatFragmentArgs>()
    private val chatView: ChatView by viewModels()
    private lateinit var mensagemAdapter: MensagemAdapter
    private lateinit var emissorRoom: String
    private lateinit var receptorRoom: String
    private val currentUserId = Firebase.auth.currentUser!!.uid
    private lateinit var currentUser: User

    /**
     * Método chamado para criar a visualização do fragmento.
     *
     * @param inflater O inflador para inflar o layout.
     * @param container O contêiner pai para o layout do fragmento.
     * @param savedInstanceState Um objeto Bundle contendo os dados da última instância do fragmento.
     * @return Retorna a visualização do fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TelaChatBinding.inflate(inflater, container, false)

        // Configuração do presenter para o Firebase Realtime Database
        chatView.repo.firebaseRealTimeDatabase.firebaseRealTimeChatPresenter = this

        // Obtém dados do usuário atual
        chatView.getCurrentUser()

        // Configuração das salas de emissor e receptor para o chat
        emissorRoom = currentUserId + args.user.id
        receptorRoom = args.user.id + currentUserId
        chatView.getMessages(emissorRoom)

        return binding.root
    }

    /**
     * Método chamado após a criação da visualização do fragmento.
     *
     * @param view A visualização do fragmento.
     * @param savedInstanceState Um objeto Bundle contendo os dados da última instância do fragmento.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa as alterações no LiveData do usuário atual
        chatView.currentUserLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                currentUser = it
            }
        }

        // Configuração da UI e observação das mensagens no LiveData
        setDataInUi()
        setUpRecyclerView()

        chatView.messagesLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                mensagemAdapter.setMensagensListToAdapter(it)
                binding.chatRecyclerview.scrollToPosition(mensagemAdapter.itemCount - 1)
            }
        }

        // Configuração do botão de voltar
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            goToLatestMessageFragment()
        }

        // Configuração do clique no botão de voltar
        binding.botaoVoltarImagebuttom.setOnClickListener {
            goToLatestMessageFragment()
        }

        // Configuração do clique no botão de enviar mensagem
        binding.enviarMensagemButton.setOnClickListener {
            val message = binding.campoMensagemEdittext.text.toString()
            if (message.isNotBlank() && message.isNotEmpty()) {
                chatView.sendMessage(message, emissorRoom, receptorRoom, args.user, currentUser)
                binding.campoMensagemEdittext.setText("")
            } else {
                binding.campoMensagemEdittext.error = "Digite a mensagem"
            }
        }
    }

    /**
     * Método para configurar os dados do usuário na UI.
     */
    private fun setDataInUi() {
        binding.nomeDoUsuarioTextview.text = args.user.username
        if (args.user.perfilImgUrl != null) {
            binding.imagemUsuarioCircleimageview.load(args.user.perfilImgUrl)
        }
    }

    /**
     * Método para configurar o RecyclerView e o adaptador de mensagens.
     */
    private fun setUpRecyclerView() {
        mensagemAdapter = MensagemAdapter(requireContext(), args.user.id)
        binding.chatRecyclerview.adapter = mensagemAdapter
        binding.chatRecyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.chatRecyclerview.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 200
        }
    }

    /**
     * Método para navegar até o fragmento de últimas mensagens.
     */
    private fun goToLatestMessageFragment() {
        val action = ChatFragmentDirections.actionChatFragmentToLatestMessagesFragment()
        findNavController().navigate(action)
    }

    /**
     * Método chamado quando a mensagem é salva no banco de dados com sucesso ou não.
     *
     * @param ifSuccess Indica se a operação foi bem-sucedida.
     * @param state Mensagem de estado.
     */
    override fun ifSaveMessageToDatabaseSuccess(ifSuccess: Boolean, state: String) {
        if (ifSuccess) {
            binding.chatRecyclerview.scrollToPosition(mensagemAdapter.itemCount - 1)
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
        }
    }

}