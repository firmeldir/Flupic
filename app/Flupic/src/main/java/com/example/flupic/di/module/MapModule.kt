package com.example.flupic.di.module

import android.content.Context
import com.example.flupic.repository.MapRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class MapModule{

    @Provides
    fun provideMapRepository(a: FirebaseAuth,
                             f: FirebaseFirestore,
                             s: FirebaseStorage,
                             l: FusedLocationProviderClient,
                             c: Context): MapRepository
            = MapRepository(a, f, s, l, c)
}