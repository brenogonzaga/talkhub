package com.ifam.talkhub.ui.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.data.model.UserUltimaMensagem
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
class UltimasMensagensView @Inject constructor(
    val repo: FirebaseRepoImp
) : ViewModel() {

    // LiveData contendo a URL da imagem do perfil do usuário atual.
    private var _currentUserProfileImageMutableLiveData: MutableLiveData<String?> =
        MutableLiveData<String?>()
    val currentUserProfileImageLiveData: LiveData<String?> get() = _currentUserProfileImageMutableLiveData

    // LiveData contendo a lista de usuários e suas últimas mensagens.
    val latestUserAndMessagesLiveData: LiveData<ArrayList<UserUltimaMensagem?>?> get() = repo.latestUserAndMessageLiveData

    // LiveData contendo os resultados da pesquisa de usuários e suas últimas mensagens.
    val searchOnUsersLiveData: LiveData<ArrayList<UserUltimaMensagem?>?> get() = repo.searchOnUsersLiveData

    /**
     * Obtém informações do usuário atual, incluindo a URL da imagem do perfil.
     */
    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repo.getCurrentUser()
                _currentUserProfileImageMutableLiveData.postValue(user?.perfilImgUrl)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "getCurrentUser: ${e.message}")
            }
        }
    }

    /**
     * Obtém a lista de usuários e suas últimas mensagens para um usuário específico.
     *
     * @param userId ID do usuário para o qual obter as últimas mensagens.
     */
    fun getLatestUserAndMessages(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getLatestUserAndMessages(userId)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "getLatestUserAndMessages: ${e.message}")
            }
        }
    }

    /**
     * Realiza uma pesquisa por usuários e suas últimas mensagens com base no nome.
     *
     * @param senderId ID do remetente (usuário realizando a pesquisa).
     * @param name Nome a ser usado como critério de pesquisa.
     */
    fun searchOnLatestUserAndMessages(senderId: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.searchOnLatestUserAndMessages(senderId, name)
        }
    }
}
