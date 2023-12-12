package com.ifam.talkhub.ui.presenter

import android.net.Uri

/**
 * Interface que define métodos de retorno de chamadas relacionados ao armazenamento no Firebase para perfis de usuário.
 */
interface FirebaseStorageProfilePresenter {

    /**
     * Chamado quando o upload da foto para o Firebase Storage é bem-sucedido.
     *
     * @param isSuccess Indica se o upload da foto foi bem-sucedido.
     * @param statue Mensagem de status indicando o resultado da operação.
     * @param uri URI da foto após o upload (pode ser nulo em caso de falha).
     * @param imageId Identificador da foto após o upload (pode ser nulo em caso de falha).
     */
    fun isUploadPhotoSuccessful(isSuccess: Boolean, statue: String, uri: Uri?, imageId: String?)
}
