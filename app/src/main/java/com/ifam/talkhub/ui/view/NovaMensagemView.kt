package com.ifam.talkhub.ui.view

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
 * ViewModel responsável por gerenciar a lógica de negócios relacionada à criação de novas mensagens.
 *
 * @property repo Repositório que fornece métodos para interagir com os dados do Firebase.
 */
@HiltViewModel
class NovaMensagemView @Inject constructor(
    val repo: FirebaseRepoImp
) : ViewModel() {
    private var _usersMutableLiveData: MutableLiveData<ArrayList<User>?> =
        MutableLiveData<ArrayList<User>?>()
    val userLiveData: LiveData<ArrayList<User>?> get() = _usersMutableLiveData
    val searchUsersLiveData: LiveData<ArrayList<User?>> = repo.searchUsersLiveData

    /**
     * Obtém a lista de todos os usuários e atualiza o LiveData correspondente.
     */
    fun getAllUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val users = repo.getAllUser()
                _usersMutableLiveData.postValue(users as ArrayList<User>)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "getAllUsers: ${e.message}")
            }
        }
    }

    /**
     * Realiza uma pesquisa de usuários com base no nome fornecido e atualiza o LiveData correspondente.
     *
     * @param name Nome do usuário a ser pesquisado.
     */
    fun searchOnUsers(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.searchOnUsers(name)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "searchOnUsers: ${e.message}")
            }
        }
    }
}
