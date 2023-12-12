package com.ifam.talkhub.ui.view

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
 * ViewModel responsável por gerenciar a lógica de negócios relacionada ao processo de login.
 *
 * @property repo Repositório que fornece métodos para interagir com os dados do Firebase.
 */
@HiltViewModel
class LoginView @Inject constructor(
    val repo: FirebaseRepoImp
) : ViewModel() {

    /**
     * Inicia o processo de login utilizando e-mail e senha fornecidos.
     *
     * @param email E-mail do usuário.
     * @param password Senha do usuário.
     */
    fun singInWithEmailPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.singInWithEmailPassword(email, password)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "singInWithEmailPassword: ${e.message}")
            }
        }
    }

    /**
     * Verifica se o e-mail do usuário foi verificado.
     */
    fun checkEmailVerification() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                repo.checkEmailVerification()
            } catch (e: Exception) {
                Log.d(Constants.TAG, "checkEmailVerification: ${e.message}")
            }
        }
    }

    /**
     * Envia um e-mail de verificação de login para o usuário atual.
     */
    fun sendEmailVerificationLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.sendEmailVerificationLogin()
            } catch (e: Exception) {
                Log.d(Constants.TAG, "sendEmailVerificationLogin: ${e.message}")
            }
        }
    }
}
