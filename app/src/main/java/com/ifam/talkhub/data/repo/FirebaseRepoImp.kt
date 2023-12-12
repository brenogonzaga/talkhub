package com.ifam.talkhub.data.repo

import android.net.Uri
import androidx.lifecycle.LiveData
import com.ifam.talkhub.data.model.UserUltimaMensagem
import com.ifam.talkhub.data.model.Mensagem
import com.ifam.talkhub.data.model.User
import com.ifam.talkhub.data.network.*
import javax.inject.Inject

/**
 * Implementação da interface [FirebaseAuthRepo], [FirebaseStorageSaveDataRepo],
 * [FirebaseFireStoreSaveDataRepo], [FirebaseRetrieveFromFireStoreRepo],
 * [FirebaseRealTimeDatabaseRepo], [FirebaseStorageProfileRepo].
 *
 * @property firebaseAuthentication Instância de [FirebaseAuthentication] para operações de autenticação.
 * @property firebaseStorageSaveDataRegister Instância de [FirebaseStorageSaveDataRegister] para operações de armazenamento de dados durante o registro.
 * @property firebaseFireStoreSaveData Instância de [FirebaseFireStoreSaveData] para operações de armazenamento no Firestore.
 * @property firebaseRetrieveFromFireStore Instância de [FirebaseRetrieveFromFireStore] para operações de recuperação de dados do Firestore.
 * @property firebaseRealTimeDatabase Instância de [FirebaseRealTimeDatabase] para operações em tempo real no banco de dados.
 * @property firebaseStorageProfile Instância de [FirebaseStorageProfile] para operações de armazenamento relacionadas a perfis.
 */
class FirebaseRepoImp @Inject constructor(
    val firebaseAuthentication: FirebaseAuthentication,
    val firebaseStorageSaveDataRegister: FirebaseStorageSaveDataRegister,
    val firebaseFireStoreSaveData: FirebaseFireStoreSaveData,
    val firebaseRetrieveFromFireStore: FirebaseRetrieveFromFireStore,
    val firebaseRealTimeDatabase: FirebaseRealTimeDatabase,
    val firebaseStorageProfile: FirebaseStorageProfile
) : FirebaseAuthRepo,
    FirebaseStorageSaveDataRepo,
    FirebaseFireStoreSaveDataRepo,
    FirebaseRetrieveFromFireStoreRepo,
    FirebaseRealTimeDatabaseRepo,
    FirebaseStorageProfileRepo {

    // Implementações dos métodos da interface FirebaseAuthRepo
    override suspend fun createNewUserAccount(email: String, password: String) {
        firebaseAuthentication.createNewUserAccount(email, password)
    }

    override suspend fun sendEmailVerificationRegister() {
        firebaseAuthentication.sendEmailVerificationRegister()
    }

    override suspend fun singInWithEmailPassword(email: String, password: String) {
        firebaseAuthentication.singInWithEmailPassword(email, password)
    }

    override suspend fun checkEmailVerification() {
        firebaseAuthentication.checkEmailVerification()
    }

    override suspend fun sendEmailVerificationLogin() {
        firebaseAuthentication.sendEmailVerificationLogin()
    }

    // Implementações dos métodos da interface FirebaseStorageSaveDataRepo

    override suspend fun uploadPhotoToFirebaseStorage(uri: Uri) {
        firebaseStorageSaveDataRegister.uploadPhotoToFirebaseStorage(uri)
    }

    // Implementações dos métodos da interface FirebaseFireStoreSaveDataRepo

    override suspend fun insertUserToFireStoreDB(username: String, email: String, uri: String?, imageId: String?) {
        firebaseFireStoreSaveData.insertUserToFireStoreDB(username, email, uri, imageId)
    }

    override suspend fun updateUserInFireStoreDB(user: User) {
        firebaseFireStoreSaveData.updateUserInFireStoreDB(user)
    }

    //// Implementações dos métodos da interface FirebaseRetrieveFromFireStoreRepo

    override suspend fun getAllUser(): List<User>? {
        return firebaseRetrieveFromFireStore.getAllUser()
    }

    override suspend fun getCurrentUser(): User? {
        return firebaseRetrieveFromFireStore.getCurrentUser()
    }

    override suspend fun searchOnUsers(name: String) {
        firebaseRetrieveFromFireStore.searchOnUsers(name)
    }

    override val searchUsersLiveData: LiveData<ArrayList<User?>>
        get() = firebaseRetrieveFromFireStore.searchOnUsersMutableLiveData

    // Implementações dos métodos da interface FirebaseRealTimeDatabaseRepo

    override suspend fun sendMessage(message: String, senderRoom: String, receiverRoom: String, userReceiver: User, userSender: User) {
        firebaseRealTimeDatabase.sendMessage(message, senderRoom, receiverRoom, userReceiver, userSender)
    }

    override suspend fun getMessages(senderRoom: String) {
        firebaseRealTimeDatabase.getMessages(senderRoom)
    }

    override val messagesLiveData: LiveData<ArrayList<Mensagem?>?>
        get() = firebaseRealTimeDatabase.messagesListMutableLiveData

    override suspend fun getLatestUserAndMessages(senderId: String) {
        firebaseRealTimeDatabase.getLatestUserAndMessages(senderId)
    }

    override val latestUserAndMessageLiveData: LiveData<ArrayList<UserUltimaMensagem?>?>
        get() = firebaseRealTimeDatabase.latestUserAndMessagesListMutableLiveData

    override suspend fun searchOnLatestUserAndMessages(senderId: String, name: String) {
        firebaseRealTimeDatabase.searchOnLatestUserAndMessages(senderId, name)
    }

    override val searchOnUsersLiveData: LiveData<ArrayList<UserUltimaMensagem?>?>
        get() = firebaseRealTimeDatabase.searchOnUsersMutableLiveData

    // Implementações dos métodos da interface FirebaseStorageProfileRepo

    override suspend fun uploadPhotoToFirebaseStorageProfile(uri: Uri) {
        firebaseStorageProfile.uploadPhotoToFirebaseStorage(uri)
    }

    override suspend fun deleteImageFromFireStorageProfile(fileName: String) {
        firebaseStorageProfile.deleteImageFromFireStorage(fileName)
    }

}