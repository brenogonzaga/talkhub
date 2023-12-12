package com.ifam.talkhub.data.repo

import androidx.lifecycle.LiveData
import com.ifam.talkhub.data.model.User

/**
 * Interface que define métodos para recuperar dados do Firestore.
 */
interface FirebaseRetrieveFromFireStoreRepo {
    /**
     * Recupera todos os usuários armazenados no Firestore.
     *
     * @return Lista de usuários ou null em caso de falha.
     */
    suspend fun getAllUser(): List<User>?

    /**
     * Recupera o usuário atualmente autenticado no Firestore.
     *
     * @return O usuário atual ou null em caso de falha.
     */
    suspend fun getCurrentUser(): User?

    /**
     * Realiza uma pesquisa de usuários com base no nome no Firestore.
     *
     * @param name Nome para ser usado como critério de pesquisa.
     */
    suspend fun searchOnUsers(name: String)

    /**
     * LiveData contendo a lista de usuários resultante da pesquisa.
     */
    val searchUsersLiveData: LiveData<ArrayList<User?>>
}
