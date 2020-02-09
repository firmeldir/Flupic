package com.example.flupic.di.module

import com.example.flupic.data.auth.AuthUserDataSource
import com.example.flupic.data.auth.FirebaseAuthUserDataSource
import com.example.flupic.data.user.FirebaseUserManagementDataSource
import com.example.flupic.data.user.UserManagementDataSource
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

    @Provides
    fun providePickPhotoViewModelDelegate(): PickPhotoViewModelDelegate {
        return ExternalStoragePickPhotoViewModelDelegate()
    }
}