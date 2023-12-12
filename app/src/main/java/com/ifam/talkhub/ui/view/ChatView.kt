package com.ifam.talkhub.ui.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.data.model.Mensagem
import com.ifam.talkhub.data.model.User
import com.ifam.talkhub.data.repo.FirebaseRepoImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsável por gerenciar a lógica de negócios relacionada à funcionalidade de chat.
 *
 * @property repo Repositório que fornece métodos para interagir com os dados do Firebase.
 */
@HiltViewModel
class ChatView @Inject constructor(
    val repo: FirebaseRepoImp
) : ViewModel() {

    /**
     * LiveData contendo a lista de mensagens do chat.
     */
    val messagesLiveData: LiveData<ArrayList<Mensagem?>?>
        get() = repo.messagesLiveData

    /**
     * LiveData contendo as informações do usuário atual.
     */
    private var _currentUserMutableLiveData: MutableLiveData<User?> = MutableLiveData()
    val currentUserLiveData: LiveData<User?> get() = _currentUserMutableLiveData

    /**
     * Envia uma mensagem no chat entre dois usuários.
     *
     * @param message Conteúdo da mensagem.
     * @param senderRoom Sala do remetente.
     * @param receiverRoom Sala do destinatário.
     * @param userReceiver Usuário destinatário.
     * @param userSender Usuário remetente.
     */
    fun sendMessage(message: String, senderRoom: String, receiverRoom: String, userReceiver: User, userSender: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.sendMessage(message, senderRoom, receiverRoom, userReceiver, userSender)
        }
    }

    /**
     * Obtém as mensagens de uma sala específica do chat.
     *
     * @param senderRoom Sala do remetente.
     */
    fun getMessages(senderRoom: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getMessages(senderRoom)
        }
    }

    /**
     * Obtém as informações do usuário atual.
     */
    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repo.getCurrentUser()
                _currentUserMutableLiveData.postValue(user)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "getCurrentUser: ${e.message}")
            }
        }
    }
}
