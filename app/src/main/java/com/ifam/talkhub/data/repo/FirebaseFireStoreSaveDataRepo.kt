package com.ifam.talkhub.data.repo

import com.ifam.talkhub.data.model.User

/**
 * Interface que define métodos para operações de armazenamento de dados no Firestore.
 */
interface FirebaseFireStoreSaveDataRepo {
    /**
     * Insere um novo usuário no Firestore.
     *
     * @param username Nome de usuário do novo usuário.
     * @param email Endereço de e-mail do novo usuário.
     * @param uri URL da imagem de perfil do novo usuário (opcional).
     * @param imageId Identificador da imagem de perfil do novo usuário (opcional).
     */
    suspend fun insertUserToFireStoreDB(username: String, email: String, uri: String?, imageId: String?)

    /**
     * Atualiza as informações do usuário no Firestore.
     *
     * @param user Instância do [User] com as informações atualizadas.
     */
    suspend fun updateUserInFireStoreDB(user: User)
}