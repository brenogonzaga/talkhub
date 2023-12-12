package com.ifam.talkhub.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.data.model.User
import com.ifam.talkhub.ui.presenter.FirebaseFireStoreRegisterPresenter
import com.ifam.talkhub.ui.presenter.FirebaseFireStoreSaveDataProfilePresenter
import javax.inject.Inject

/**
 * Classe responsável por interagir com o Firestore do Firebase para salvar e atualizar dados relacionados aos usuários.
 *
 * @property firebaseCollection Instância do [FirebaseFirestore] utilizada para acessar coleções no Firestore.
 */
class FirebaseFireStoreSaveData @Inject constructor(
    private val firebaseCollection: FirebaseFirestore
) {
    /**
     * Presenter para manipulação de eventos relacionados ao registro de usuário no Firestore.
     */
    var firebaseFireStoreRegisterPresenter: FirebaseFireStoreRegisterPresenter? = null
    /**
     * Presenter para manipulação de eventos relacionados à atualização de dados de perfil no Firestore.
     */
    var firebaseFireStoreSaveDataProfilePresenter: FirebaseFireStoreSaveDataProfilePresenter? = null

    /**
     * Insere um novo usuário no Firestore.
     *
     * @param username Nome de usuário do novo usuário.
     * @param email E-mail do novo usuário.
     * @param uri URL da imagem de perfil do novo usuário.
     * @param imageId Identificador único da imagem de perfil do novo usuário.
     */
    fun insertUserToFireStoreDB(username: String, email: String, uri: String?, imageId: String?) {
        val uid = FirebaseAuth.getInstance().uid ?: return
        val myRef = firebaseCollection.collection("users")
        val user = User(uid, username, email, uri, imageId)

        myRef.document(uid).set(user)
            .addOnSuccessListener {
                firebaseFireStoreRegisterPresenter!!.ifUserInsertedSuccess(
                    true,
                    Constants.SUCCESS_MESSAGE
                )
            }
            .addOnFailureListener {
                firebaseFireStoreRegisterPresenter!!.ifUserInsertedSuccess(
                    false,
                    it.message!!
                )
            }
    }

    /**
     * Atualiza os dados de um usuário no Firestore.
     *
     * @param user Instância do [User] contendo os dados atualizados.
     */
    fun updateUserInFireStoreDB(user: User) {
        val myRef = firebaseCollection.collection("users")

        myRef.document(user.id).update(
            "username", user.username,
            "perfilImgUrl", user.perfilImgUrl,
            "perfilImgId", user.perfilImgId
        ).addOnSuccessListener {
            firebaseFireStoreSaveDataProfilePresenter?.isUpdateUserFromFireStoreSuccess(
                true,
                Constants.SUCCESS_MESSAGE
            )
        }
            .addOnFailureListener {
                firebaseFireStoreSaveDataProfilePresenter?.isUpdateUserFromFireStoreSuccess(
                    false,
                    it.message!!
                )
            }
    }

}