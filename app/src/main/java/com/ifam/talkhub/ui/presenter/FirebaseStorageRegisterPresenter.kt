package com.ifam.talkhub.ui.presenter

import android.net.Uri

/**
 * Interface que define métodos de retorno de chamadas relacionados ao armazenamento no Firebase durante o registro.
 */
interface FirebaseStorageRegisterPresenter {

    /**
     * Chamado quando o upload da imagem para o Firebase Storage é bem-sucedido.
     *
     * @param ifSuccess Indica se o upload da imagem foi bem-sucedido.
     * @param state Mensagem de estado indicando o resultado da operação.
     * @param uri URI da imagem após o upload (pode ser nulo em caso de falha).
     * @param imageId Identificador da imagem após o upload (pode ser nulo em caso de falha).
     */
    fun ifImageUploadedSuccess(ifSuccess: Boolean, state: String, uri: Uri?, imageId: String?)
}
