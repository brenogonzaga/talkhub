package com.ifam.talkhub.data.network

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.ui.presenter.FirebaseStorageProfilePresenter
import java.util.*
import javax.inject.Inject

/**
 * Classe responsável por interagir com o Firebase Storage para operações relacionadas a perfis de usuário.
 *
 * @property firebaseStorageInstance Instância do [FirebaseStorage] utilizada para referenciar o Storage.
 */
class FirebaseStorageProfile @Inject constructor(
    private val firebaseStorageInstance: FirebaseStorage
) {
    /**
     * Presenter para manipulação de eventos relacionados ao armazenamento no Firebase Storage.
     */
    var firebaseStorageProfilePresenter: FirebaseStorageProfilePresenter? = null

    /**
     * Faz o upload de uma foto para o Firebase Storage.
     *
     * @param uri URI da imagem a ser carregada.
     */
    fun uploadPhotoToFirebaseStorage(uri: Uri) {
        val fileName = UUID.randomUUID().toString()
        val myRef = firebaseStorageInstance.getReference("/images/$fileName")
        myRef.putFile(uri)
            .addOnSuccessListener {
                myRef.downloadUrl
                    .addOnSuccessListener {
                        firebaseStorageProfilePresenter?.isUploadPhotoSuccessful(
                            true,
                            Constants.TAG,
                            it,
                            fileName
                        )
                    }
                    .addOnFailureListener {
                        firebaseStorageProfilePresenter?.isUploadPhotoSuccessful(
                            false,
                            it.message!!,
                            null,
                            null
                        )
                    }
            }
            .addOnCanceledListener {
                firebaseStorageProfilePresenter?.isUploadPhotoSuccessful(
                    false,
                    Constants.FAILED_MESSAGE,
                    null,
                    null
                )
            }
    }

    /**
     * Exclui uma imagem do Firebase Storage.
     *
     * @param fileName Nome do arquivo a ser excluído.
     */
    fun deleteImageFromFireStorage(fileName: String) {
        val myRef = firebaseStorageInstance.getReference("/images/$fileName")
        myRef.delete()
            .addOnSuccessListener {
                Log.d(Constants.TAG, "deleteImageFromFireStorage: ${Constants.SUCCESS_MESSAGE}")
            }
            .addOnFailureListener {
                Log.d(Constants.TAG, "deleteImageFromFireStorage: ${it.message}")
            }
    }

}