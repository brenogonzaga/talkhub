package com.ifam.talkhub.data.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.data.model.UserUltimaMensagem
import com.ifam.talkhub.data.model.Mensagem
import com.ifam.talkhub.data.model.User
import com.ifam.talkhub.ui.presenter.FirebaseRealTimeChatPresenter
import com.ifam.talkhub.utils.getCurrentDate
import javax.inject.Inject

/**
 * Classe responsável por interagir com o Firebase Realtime Database para operações relacionadas a chat.
 *
 * @property myRef Instância do [DatabaseReference] utilizada para referenciar o nó principal do banco de dados.
 */
class FirebaseRealTimeDatabase @Inject constructor(
    private val myRef: DatabaseReference
) {
    /**
     * Presenter para manipulação de eventos relacionados a chats em tempo real.
     */
    var firebaseRealTimeChatPresenter: FirebaseRealTimeChatPresenter? = null
    /**
     * Lista mutável para armazenar mensagens.
     */
    var messagesListMutableLiveData: MutableLiveData<ArrayList<Mensagem?>?> = MutableLiveData<ArrayList<Mensagem?>?>()
    /**
     * Lista mutável para armazenar as últimas mensagens e usuários.
     */
    var latestUserAndMessagesListMutableLiveData: MutableLiveData<ArrayList<UserUltimaMensagem?>?> = MutableLiveData<ArrayList<UserUltimaMensagem?>?>()
    /**
     * Lista mutável para armazenar resultados de pesquisa de usuários e mensagens.
     */
    var searchOnUsersMutableLiveData: MutableLiveData<ArrayList<UserUltimaMensagem?>?> = MutableLiveData<ArrayList<UserUltimaMensagem?>?>()

    /**
     * Envia uma mensagem entre usuários no chat.
     *
     * @param message Conteúdo da mensagem.
     * @param senderRoom Identificador da sala do remetente.
     * @param receiverRoom Identificador da sala do destinatário.
     * @param userReceiver Usuário destinatário.
     * @param userSender Usuário remetente.
     */
    fun sendMessage(
        message: String,
        senderRoom: String,
        receiverRoom: String,
        userReceiver: User,
        userSender: User
    ) {
        val theMensagem = Mensagem(message, userSender.id, userReceiver.id, getCurrentDate())
        val userUltimaMensagemForSender = UserUltimaMensagem(theMensagem, userReceiver)
        val userUltimaMensagemForReceiver = UserUltimaMensagem(theMensagem, userSender)
        myRef.child("chats").child(senderRoom).child("messages").push()
            .setValue(theMensagem).addOnSuccessListener {
                myRef.child("chats").child(receiverRoom).child("messages").push()
                    .setValue(theMensagem).addOnSuccessListener {

                        myRef.child("latestUsersAndMessages").child(userSender.id)
                            .child(userReceiver.id)
                            .setValue(userUltimaMensagemForSender)
                            .addOnSuccessListener {
                                myRef.child("latestUsersAndMessages").child(userReceiver.id)
                                    .child(userSender.id)
                                    .setValue(userUltimaMensagemForReceiver)
                                    .addOnSuccessListener {
                                        firebaseRealTimeChatPresenter?.ifSaveMessageToDatabaseSuccess(
                                            true,
                                            Constants.SUCCESS_MESSAGE
                                        )
                                    }
                                    .addOnFailureListener {
                                        firebaseRealTimeChatPresenter?.ifSaveMessageToDatabaseSuccess(
                                            false,
                                            it.message!!
                                        )
                                    }
                            }
                            .addOnFailureListener {
                                firebaseRealTimeChatPresenter?.ifSaveMessageToDatabaseSuccess(
                                    false,
                                    it.message!!
                                )
                            }
                    }
                    .addOnFailureListener {
                        firebaseRealTimeChatPresenter?.ifSaveMessageToDatabaseSuccess(
                            false,
                            it.message!!
                        )
                    }
            }
            .addOnFailureListener {
                firebaseRealTimeChatPresenter?.ifSaveMessageToDatabaseSuccess(
                    false,
                    it.message!!
                )
            }
    }

    /**
     * Obtém as mensagens de uma sala específica.
     *
     * @param senderRoom Identificador da sala do remetente.
     */
    fun getMessages(senderRoom: String) {
        val mensagems = ArrayList<Mensagem?>()
        myRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        mensagems.clear()
                        snapshot.children.forEach {
                            val mensagem = it.getValue(Mensagem::class.java)
                            mensagems.add(mensagem)
                        }
                        messagesListMutableLiveData.postValue(mensagems)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(Constants.TAG, "onCancelled: ${error.message}")
                }
            })
    }

    /**
     * Obtém as últimas mensagens e usuários para um remetente específico.
     *
     * @param senderId Identificador do remetente.
     */
    fun getLatestUserAndMessages(senderId: String) {
        val latestUserAndMessageList = ArrayList<UserUltimaMensagem?>()
        myRef.child("latestUsersAndMessages").child(senderId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        latestUserAndMessageList.clear()
                        snapshot.children.forEach {
                            val userUltimaMensagem = it.getValue(UserUltimaMensagem::class.java)
                            latestUserAndMessageList.add(userUltimaMensagem)
                        }
                        latestUserAndMessagesListMutableLiveData.postValue(latestUserAndMessageList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(Constants.TAG, "onCancelled: ${error.message}")
                }
            })
    }

    /**
     * Realiza uma pesquisa nas últimas mensagens e usuários para um remetente específico com base no nome.
     *
     * @param senderId Identificador do remetente.
     * @param name Nome a ser pesquisado.
     */
    fun searchOnLatestUserAndMessages(senderId: String, name: String) {
        val latestUserAndMessageList = ArrayList<UserUltimaMensagem?>()
        myRef.child("latestUsersAndMessages").child(senderId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        latestUserAndMessageList.clear()
                        snapshot.children.forEach {
                            val userUltimaMensagem = it.getValue(UserUltimaMensagem::class.java)
                            if (name.lowercase() in userUltimaMensagem!!.user.username.lowercase()) {
                                latestUserAndMessageList.add(userUltimaMensagem)
                            }
                        }
                        searchOnUsersMutableLiveData.postValue(latestUserAndMessageList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(Constants.TAG, "onCancelled: ${error.message}")
                }
            })
    }

}