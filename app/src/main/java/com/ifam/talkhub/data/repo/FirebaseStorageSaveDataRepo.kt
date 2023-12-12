package com.ifam.talkhub.data.repo

import android.net.Uri

/**
 * Interface que define métodos para operações de armazenamento no Firebase Storage durante o registro.
 */
interface FirebaseStorageSaveDataRepo {
    /**
     * Faz o upload de uma foto para o Firebase Storage durante o registro.
     *
     * @param uri URI da imagem a ser enviada para o armazenamento.
     */
    suspend fun uploadPhotoToFirebaseStorage(uri: Uri)
}
