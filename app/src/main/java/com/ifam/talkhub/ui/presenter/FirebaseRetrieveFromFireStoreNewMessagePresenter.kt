package com.ifam.talkhub.ui.presenter

/**
 * Interface que define métodos de retorno de chamadas relacionados à recuperação de dados do Firestore para novas mensagens.
 */
interface FirebaseRetrieveFromFireStoreNewMessagePresenter {

    /**
     * Chamado quando a recuperação de dados do Firestore é bem-sucedida.
     *
     * @param isSuccess Indica se a recuperação de dados do Firestore foi bem-sucedida.
     * @param state Mensagem de estado indicando o resultado da operação.
     */
    fun ifRetrieveFromFirebaseSuccess(isSuccess: Boolean, state: String)
}
