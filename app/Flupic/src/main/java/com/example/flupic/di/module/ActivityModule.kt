package com.example.flupic.di.module

import com.example.flupic.repository.InsideRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class ActivityModule{

    @Provides
    fun provideInsideRepository(a: FirebaseAuth, f: FirebaseFirestore, s: FirebaseStorage): InsideRepository
            = InsideRepository(a, f, s)
}