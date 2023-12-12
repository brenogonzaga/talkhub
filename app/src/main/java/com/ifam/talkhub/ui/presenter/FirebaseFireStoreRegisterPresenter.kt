package com.ifam.talkhub.ui.presenter

/**
 * Interface que define métodos de retorno de chamadas relacionados à operação de inserção de usuário no Firestore.
 */
interface FirebaseFireStoreRegisterPresenter {

    /**
     * Chamado quando a inserção de usuário no Firestore é concluída com sucesso.
     *
     * @param ifSuccess Indica se a operação de inserção foi bem-sucedida.
     * @param state Mensagem de estado indicando o resultado da operação.
     */
    fun ifUserInsertedSuccess(ifSuccess: Boolean, state: String)
}
