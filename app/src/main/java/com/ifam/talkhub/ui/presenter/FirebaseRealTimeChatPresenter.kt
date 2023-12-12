package com.ifam.talkhub.ui.presenter

/**
 * Interface que define métodos de retorno de chamadas relacionados à comunicação em tempo real do chat.
 */
interface FirebaseRealTimeChatPresenter {

    /**
     * Chamado quando a mensagem é salva com sucesso no banco de dados em tempo real.
     *
     * @param ifSuccess Indica se a operação de salvar a mensagem foi bem-sucedida.
     * @param state Mensagem de estado indicando o resultado da operação.
     */
    fun ifSaveMessageToDatabaseSuccess(ifSuccess: Boolean, state: String)
}
