package com.ifam.talkhub.data.network

import com.google.firebase.auth.FirebaseAuth
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.ui.presenter.FirebaseAuthLoginPresenter
import com.ifam.talkhub.ui.presenter.FirebaseAuthRegisterPresenter
import javax.inject.Inject

/**
 * Classe responsável pela autenticação via Firebase.
 *
 * @property auth Instância do [FirebaseAuth] utilizada para autenticação.
 */
class FirebaseAuthentication @Inject constructor(
    private val auth: FirebaseAuth
) {
    /**
     * Presenter para manipulação de eventos relacionados ao registro de usuário.
     */
    var firebaseAuthRegisterPresenter: FirebaseAuthRegisterPresenter? = null
    /**
     * Presenter para manipulação de eventos relacionados ao login de usuário.
     */
    var firebaseAuthLoginPresenter: FirebaseAuthLoginPresenter? = null

    /**
     * Cria uma nova conta de usuário com o e-mail e senha fornecidos.
     *
     * @param email E-mail do novo usuário.
     * @param password Senha do novo usuário.
     */
    fun createNewUserAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseAuthRegisterPresenter!!.ifCreateNewUserAccountSuccess(
                        true,
                        Constants.SUCCESS_MESSAGE
                    )
                } else {
                    firebaseAuthRegisterPresenter!!.ifCreateNewUserAccountSuccess(
                        false,
                        it.exception!!.message!!
                    )
                }
            }
    }

    /**
     * Envia um e-mail de verificação para o usuário recém-registrado.
     */
    fun sendEmailVerificationRegister() {
        val currentUser = auth.currentUser
        currentUser!!.sendEmailVerification()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseAuthRegisterPresenter!!.ifSendEmailVerificationSuccessRegister(
                        true,
                        Constants.SUCCESS_MESSAGE
                    )
                } else {
                    firebaseAuthRegisterPresenter!!.ifSendEmailVerificationSuccessRegister(
                        true,
                        it.exception!!.message!!
                    )
                }
            }
    }

    /**
     * Realiza o login do usuário com o e-mail e senha fornecidos.
     *
     * @param email E-mail do usuário.
     * @param password Senha do usuário.
     */

    fun singInWithEmailPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseAuthLoginPresenter!!.ifSingInWithEmailPasswordComplete(
                        true,
                        Constants.SUCCESS_MESSAGE
                    )
                } else {
                    firebaseAuthLoginPresenter!!.ifSingInWithEmailPasswordComplete(
                        false,
                        it.exception!!.message!!
                    )
                }
            }
    }

    /**
     * Verifica se o e-mail do usuário foi verificado.
     */

    fun checkEmailVerification() {
        val currentUser = auth.currentUser
        if (currentUser!!.isEmailVerified) {
            firebaseAuthLoginPresenter!!.ifEmailVerificationLogin(true)
        } else {
            firebaseAuthLoginPresenter!!.ifEmailVerificationLogin(false)
        }
    }

    /**
     * Envia um e-mail de verificação para o usuário durante o processo de login.
     */

    fun sendEmailVerificationLogin() {
        val currentUser = auth.currentUser
        currentUser!!.sendEmailVerification()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseAuthLoginPresenter!!.ifSendEmailVerificationSentSuccessLogin(
                        true,
                        Constants.SUCCESS_MESSAGE
                    )
                } else {
                    firebaseAuthLoginPresenter!!.ifSendEmailVerificationSentSuccessLogin(
                        true,
                        it.exception!!.message!!
                    )
                }
            }
    }
}