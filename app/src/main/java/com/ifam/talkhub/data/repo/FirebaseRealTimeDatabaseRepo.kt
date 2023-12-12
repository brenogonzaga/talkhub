package com.ifam.talkhub.data.repo

import androidx.lifecycle.LiveData
import com.ifam.talkhub.data.model.UserUltimaMensagem
import com.ifam.talkhub.data.model.Mensagem
import com.ifam.talkhub.data.model.User

/**
 * Interface que define métodos para operações no Firebase Realtime Database relacionadas a mensagens e usuários.
 */
interface FirebaseRealTimeDatabaseRepo {
    /**
     * LiveData que contém uma lista de mensagens.
     */
    val messagesLiveData: LiveData<ArrayList<Mensagem?>?>

    /**
     * LiveData que contém uma lista de usuários e suas últimas mensagens.
     */
    val latestUserAndMessageLiveData: LiveData<ArrayList<UserUltimaMensagem?>?>

    /**
     * LiveData que contém uma lista de usuários resultante de uma pesquisa.
     */
    val searchOnUsersLiveData: LiveData<ArrayList<UserUltimaMensagem?>?>

    /**
     * Envia uma mensagem entre usuários no chat.
     *
     * @param message Conteúdo da mensagem.
     * @param senderRoom Identificador da sala do remetente.
     * @param receiverRoom Identificador da sala do destinatário.
     * @param userReceiver Usuário destinatário.
     * @param userSender Usuário remetente.
     */
    suspend fun sendMessage(message: String, senderRoom: String, receiverRoom: String, userReceiver: User, userSender: User)

    /**
     * Obtém as mensagens de uma sala específica.
     *
     * @param senderRoom Identificador da sala do remetente.
     */
    suspend fun getMessages(senderRoom: String)

    /**
     * Obtém as últimas mensagens e usuários para um remetente específico.
     *
     * @param senderId Identificador do remetente.
     */
    suspend fun getLatestUserAndMessages(senderId: String)

    /**
     * Realiza uma pesquisa nas últimas mensagens e usuários para um remetente específico com base no nome.
     *
     * @param senderId Identificador do remetente.
     * @param name Nome a ser pesquisado.
     */
    suspend fun searchOnLatestUserAndMessages(senderId: String, name: String)
}
