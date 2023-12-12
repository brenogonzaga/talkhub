package com.ifam.talkhub.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Dagger responsável por fornecer instâncias únicas de componentes da rede.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    /**
     * Fornece uma instância única de [FirebaseAuth] para autenticação no Firebase.
     */
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth =
        Firebase.auth

    /**
     * Fornece uma instância única de [FirebaseStorage] para operações de armazenamento no Firebase.
     */
    @Singleton
    @Provides
    fun provideFirebaseStorageInstance(): FirebaseStorage =
        FirebaseStorage.getInstance()

    /**
     * Fornece uma instância única de [FirebaseFirestore] para operações de banco de dados no Firestore.
     */
    @Singleton
    @Provides
    fun provideFirebaseFireStore(): FirebaseFirestore =
        Firebase.firestore

    /**
     * Fornece uma referência a uma coleção específica no Firestore para operações relacionadas a usuários.
     */
    @Singleton
    @Provides
    fun provideFirebaseFireStoreCollectionReference(): CollectionReference =
        Firebase.firestore.collection("users")

    /**
     * Fornece uma instância única de [DatabaseReference] para operações em tempo real no banco de dados do Firebase.
     */
    @Singleton
    @Provides
    fun provideRealTimeDBReference(): DatabaseReference =
        FirebaseDatabase.getInstance().reference
}
