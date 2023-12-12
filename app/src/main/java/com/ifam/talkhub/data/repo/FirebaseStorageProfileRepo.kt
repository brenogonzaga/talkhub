package com.ifam.talkhub.data.repo

import android.net.Uri

/**
 * Interface que define métodos para operações de armazenamento relacionadas a perfis no Firebase Storage.
 */
interface FirebaseStorageProfileRepo {
    /**
     * Faz o upload de uma foto para o Firebase Storage.
     *
     * @param uri URI da imagem a ser enviada para o armazenamento.
     */
    suspend fun uploadPhotoToFirebaseStorageProfile(uri: Uri)

    /**
     * Exclui uma imagem do Firebase Storage.
     *
     * @param fileName Nome do arquivo a ser excluído.
     */
    suspend fun deleteImageFromFireStorageProfile(fileName: String)
}
