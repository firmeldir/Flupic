package com.example.flupic.di.module

import com.example.flupic.data.AppDatabase
import com.example.flupic.data.auth.AuthUserDataSource
import com.example.flupic.data.auth.FirebaseAuthUserDataSource
import com.example.flupic.data.users.FirebaseUserManagementDataSource
import com.example.flupic.data.users.FirebaseUsersRepository
import com.example.flupic.data.users.UserManagementDataSource
import com.example.flupic.data.users.UsersRepository
import com.example.flupic.util.ExternalStoragePickPhotoViewModelDelegate
import com.example.flupic.util.PickPhotoViewModelDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Singleton
    @Provides
    fun provideAuthUserDataSource(
        auth: FirebaseAuth
    ): AuthUserDataSource {
        return FirebaseAuthUserDataSource(auth)
    }

    @Singleton
    @Provides
    fun provideUserManagementDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): UserManagementDataSource {
        return FirebaseUserManagementDataSource(auth, firestore, storage)
    }

    @Singleton
    @Provides
    fun provideUsersRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        database: AppDatabase
    ): UsersRepository {
        return FirebaseUsersRepository(auth, firestore, database)
    }

    @Provides
    fun providePickPhotoViewModelDelegate(): PickPhotoViewModelDelegate {
        return ExternalStoragePickPhotoViewModelDelegate()
    }
}