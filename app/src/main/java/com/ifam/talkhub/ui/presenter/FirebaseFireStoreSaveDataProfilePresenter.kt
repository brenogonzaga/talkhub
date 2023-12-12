package com.ifam.talkhub.ui.presenter

/**
 * Interface que define métodos de retorno de chamadas relacionados à operação de atualização de usuário no Firestore.
 */
interface FirebaseFireStoreSaveDataProfilePresenter {

    /**
     * Chamado quando a atualização do usuário no Firestore é concluída com sucesso.
     *
     * @param isSuccess Indica se a operação de atualização foi bem-sucedida.
     * @param state Mensagem de estado indicando o resultado da operação.
     */
    fun isUpdateUserFromFireStoreSuccess(isSuccess: Boolean, state: String)
}
