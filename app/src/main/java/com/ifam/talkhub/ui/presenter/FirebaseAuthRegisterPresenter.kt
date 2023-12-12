package com.ifam.talkhub.ui.presenter

/**
 * Interface que define métodos de retorno de chamadas relacionados à autenticação e registro de usuários.
 */
interface FirebaseAuthRegisterPresenter {

    /**
     * Chamado quando a criação de uma nova conta de usuário é concluída com sucesso.
     *
     * @param ifSuccess Indica se a operação de criação de conta foi bem-sucedida.
     * @param state Mensagem de estado indicando o resultado da operação.
     */
    fun ifCreateNewUserAccountSuccess(ifSuccess: Boolean, state: String)

    /**
     * Chamado quando o envio de e-mail de verificação é concluído com sucesso durante o registro.
     *
     * @param ifSuccess Indica se a operação de envio de e-mail de verificação foi bem-sucedida.
     * @param state Mensagem de estado indicando o resultado da operação.
     */
    fun ifSendEmailVerificationSuccessRegister(ifSuccess: Boolean, state: String)
}
