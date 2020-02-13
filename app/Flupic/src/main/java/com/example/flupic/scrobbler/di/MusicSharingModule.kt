package com.example.flupic.scrobbler.di

import com.example.flupic.data.users.FirebaseUserManagementDataSource
import com.example.flupic.data.users.UserManagementDataSource
import com.example.flupic.scrobbler.data.FirebaseSharedMusicRepository
import com.example.flupic.scrobbler.data.PeriodicLocationDataSource
import com.example.flupic.scrobbler.data.PeriodicLocationDataSourceImpl
import com.example.flupic.scrobbler.data.SharedMusicRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MusicSharingModule {

    @Singleton
    @Provides
    fun provideSharedMusicRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): SharedMusicRepository {
        return FirebaseSharedMusicRepository(auth, firestore)
    }

    @Provides
    fun providePeriodicLocationDataSource(
        client: FusedLocationProviderClient
    ): PeriodicLocationDataSource {
        return PeriodicLocationDataSourceImpl(client)
    }
}