package com.ifam.talkhub.ui.presenter

/**
 * Interface que define métodos de retorno de chamadas relacionados à autenticação e login de usuários.
 */
interface FirebaseAuthLoginPresenter {

    /**
     * Chamado quando a operação de login com e-mail e senha é concluída.
     *
     * @param ifComplete Indica se a operação de login foi concluída com sucesso.
     * @param state Mensagem de estado indicando o resultado da operação.
     */
    fun ifSingInWithEmailPasswordComplete(ifComplete: Boolean, state: String)

    /**
     * Chamado para indicar o status de verificação de e-mail durante o login.
     *
     * @param ifVerified Indica se o e-mail do usuário foi verificado.
     */
    fun ifEmailVerificationLogin(ifVerified: Boolean)

    /**
     * Chamado quando o e-mail de verificação é enviado com sucesso durante o login.
     *
     * @param ifSuccess Indica se a operação de envio de e-mail de verificação foi bem-sucedida.
     * @param state Mensagem de estado indicando o resultado da operação.
     */
    fun ifSendEmailVerificationSentSuccessLogin(ifSuccess: Boolean, state: String)
}
