package com.ifam.talkhub.data.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.data.model.User
import com.ifam.talkhub.ui.presenter.FirebaseRetrieveFromFireStoreNewMessagePresenter
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Classe responsável por recuperar dados do Firestore relacionados aos usuários.
 *
 * @property myRef Instância da coleção no Firestore utilizada para referenciar os usuários.
 * @property auth Instância do [FirebaseAuth] utilizada para autenticação.
 */
class FirebaseRetrieveFromFireStore @Inject constructor(
    private val myRef: CollectionReference,
    private val auth: FirebaseAuth
) {
    /**
     * Presenter para manipulação de eventos relacionados à recuperação de novas mensagens do Firestore.
     */
    var firebaseRetrieveFromFireStoreNewMessagePresenter: FirebaseRetrieveFromFireStoreNewMessagePresenter? = null

    /**
     * Lista mutável para armazenar resultados de pesquisa de usuários.
     */
    var searchOnUsersMutableLiveData: MutableLiveData<ArrayList<User?>> = MutableLiveData<ArrayList<User?>>()

    /**
     * Obtém todos os usuários do Firestore.
     *
     * @return Lista de usuários ou null em caso de erro.
     */
    suspend fun getAllUser(): List<User>? {
        return try {
            firebaseRetrieveFromFireStoreNewMessagePresenter!!.ifRetrieveFromFirebaseSuccess(
                true,
                Constants.SUCCESS_MESSAGE
            )
            myRef.get().await().toObjects(User::class.java)
        } catch (e: Exception) {
            firebaseRetrieveFromFireStoreNewMessagePresenter!!.ifRetrieveFromFirebaseSuccess(
                false,
                e.message!!
            )
            null
        }
    }

    /**
     * Obtém o usuário atualmente autenticado.
     *
     * @return Instância do [User] representando o usuário autenticado ou null em caso de erro.
     */
    suspend fun getCurrentUser(): User? {
        val myRef = Firebase.firestore.collection("users")
        return try {
            myRef.document(auth.currentUser?.uid!!).get().await().toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Realiza uma pesquisa de usuários com base no nome.
     *
     * @param name Nome a ser pesquisado.
     */
    fun searchOnUsers(name: String) {
        val usersList = ArrayList<User?>()
        myRef.get().addOnSuccessListener { result ->
            usersList.clear()
            result.forEach {
                val res = it.toObject(User::class.java)
                if (name.lowercase() in res.username.lowercase()) {
                    usersList.add(res)
                }
            }
            searchOnUsersMutableLiveData.postValue(usersList)
        }
            .addOnFailureListener {
                Log.d(Constants.TAG, "search: ${it.message}")
            }
    }

}