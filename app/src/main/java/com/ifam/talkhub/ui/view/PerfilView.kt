package com.ifam.talkhub.ui.view

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.data.model.User
import com.ifam.talkhub.data.repo.FirebaseRepoImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsável por gerenciar a lógica de negócios relacionada ao perfil do usuário.
 *
 * @property repo Repositório que fornece métodos para interagir com os dados do Firebase.
 */
@HiltViewModel
class PerfilView @Inject constructor(
    val repo: FirebaseRepoImp
) : ViewModel() {
    private var _currentUserMutableLiveData: MutableLiveData<User?> = MutableLiveData<User?>()
    val currentUserLiveData: LiveData<User?> get() = _currentUserMutableLiveData

    /**
     * Obtém informações do usuário atual e atualiza o LiveData correspondente.
     */
    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentUser = repo.getCurrentUser()
                _currentUserMutableLiveData.postValue(currentUser)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "getCurrentUser: ${e.message}")
            }
        }
    }

    /**
     * Carrega uma foto para o perfil do usuário no armazenamento do Firebase usando a URI fornecida.
     *
     * @param uri URI da foto a ser carregada para o perfil do usuário.
     */
    fun uploadPhotoToFirebaseStorageProfile(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.uploadPhotoToFirebaseStorageProfile(uri)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "uploadPhotoToFirebaseStorageProfile: ${e.message}")
            }
        }
    }

    /**
     * Exclui uma imagem do perfil do usuário do armazenamento do Firebase.
     *
     * @param fileName Nome do arquivo da imagem a ser excluída.
     */
    fun deleteImageFromFireStorageProfile(fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteImageFromFireStorageProfile(fileName)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "deleteImageFromFireStorageProfile: ${e.message}")
            }
        }
    }

    /**
     * Atualiza as informações do usuário no Firestore.
     *
     * @param user Objeto [User] contendo as informações atualizadas do usuário.
     */
    fun updateUserInFireStoreDB(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.updateUserInFireStoreDB(user)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "updateUserInFireStoreDB: ${e.message}")
            }
        }
    }
}
