package com.ifam.talkhub.ui.view

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.data.repo.FirebaseRepoImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsável por fornecer dados relacionados às últimas mensagens e perfis de usuários.
 *
 * @property repo Repositório que fornece métodos para interagir com os dados do Firebase.
 */
@HiltViewModel
class RegistroView @Inject constructor(
    val repo: FirebaseRepoImp
) : ViewModel() {
    fun createNewUserAccount(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.createNewUserAccount(email, password)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "createNewUserAccount: ${e.message}")
            }
        }
    }

    fun sendEmailVerificationRegister() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.sendEmailVerificationRegister()
            } catch (e: Exception) {
                Log.d(Constants.TAG, "sendEmailVerificationRegister: ${e.message}")
            }
        }
    }

    fun uploadPhotoToFirebaseStorage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.uploadPhotoToFirebaseStorage(uri)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "uploadPhotoToFirebaseStorage: ${e.message}")
            }
        }
    }

    fun insertUserToFireStoreDB(username: String, email: String, uri: String?, imageId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertUserToFireStoreDB(username, email, uri, imageId)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "insertUserToFireStoreDB: ${e.message}")
            }
        }
    }

}