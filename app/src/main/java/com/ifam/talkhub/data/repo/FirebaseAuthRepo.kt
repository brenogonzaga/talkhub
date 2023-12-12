package com.ifam.talkhub.data.repo

/**
 * Interface que define métodos para operações relacionadas à autenticação no Firebase.
 */
interface FirebaseAuthRepo {

    /**
     * Cria uma nova conta de usuário com o email e senha fornecidos.
     *
     * @param email Endereço de e-mail do usuário.
     * @param password Senha do usuário.
     */
    suspend fun createNewUserAccount(email: String, password: String)

    /**
     * Envia um e-mail de verificação após o registro.
     */
    suspend fun sendEmailVerificationRegister()

    /**
     * Realiza o login do usuário com o email e senha fornecidos.
     *
     * @param email Endereço de e-mail do usuário.
     * @param password Senha do usuário.
     */
    suspend fun singInWithEmailPassword(email: String, password: String)

    /**
     * Verifica se o e-mail do usuário foi verificado.
     */
    suspend fun checkEmailVerification()

    /**
     * Envia um e-mail de verificação durante o processo de login.
     */
    suspend fun sendEmailVerificationLogin()
}