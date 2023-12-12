package com.ifam.talkhub.data.network

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.ui.presenter.FirebaseStorageRegisterPresenter
import java.util.*
import javax.inject.Inject

/**
 * Classe responsável por interagir com o Firebase Storage para operações relacionadas ao registro de usuário.
 *
 * @property firebaseStorageInstance Instância do [FirebaseStorage] utilizada para referenciar o Storage.
 */
class FirebaseStorageSaveDataRegister @Inject constructor(
    private val firebaseStorageInstance: FirebaseStorage
) {
    /**
     * Presenter para manipulação de eventos relacionados ao armazenamento no Firebase Storage durante o registro.
     */
    var firebaseStorageRegisterPresenter: FirebaseStorageRegisterPresenter? = null

    /**
     * Faz o upload de uma foto para o Firebase Storage durante o registro.
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
                        firebaseStorageRegisterPresenter!!.ifImageUploadedSuccess(
                            true,
                            Constants.SUCCESS_MESSAGE,
                            it,
                            fileName
                        )
                    }
                    .addOnFailureListener {
                        firebaseStorageRegisterPresenter!!.ifImageUploadedSuccess(
                            false,
                            it.message!!,
                            null,
                            null
                        )
                    }
            }
            .addOnCanceledListener {
                firebaseStorageRegisterPresenter!!.ifImageUploadedSuccess(
                    false,
                    Constants.FAILED_MESSAGE,
                    null,
                    null
                )
            }
    }

}