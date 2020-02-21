package com.example.flupic.scrobbler.di

import com.example.flupic.scrobbler.data.FirebaseSharingPointRepository
import com.example.flupic.scrobbler.data.PeriodicLocationDataSource
import com.example.flupic.scrobbler.data.PeriodicLocationDataSourceImpl
import com.example.flupic.scrobbler.data.SharingPointRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    ): SharingPointRepository {
        return FirebaseSharingPointRepository(auth, firestore)
    }

    @Provides
    fun providePeriodicLocationDataSource(
        client: FusedLocationProviderClient
    ): PeriodicLocationDataSource {
        return PeriodicLocationDataSourceImpl(client)
    }
}