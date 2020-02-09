package com.example.flupic.di.module

import android.app.Activity
import android.app.Application
import android.content.Context
import com.example.flupic.di.subcomponent.AuthScope
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsClient
import com.google.android.gms.auth.api.credentials.CredentialsOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import dagger.Module
import dagger.Provides

@Module
class AuthModule {

    @AuthScope
    @Provides
    fun provideCredentialsClient(context: Context): CredentialsClient{
        val options = CredentialsOptions.Builder()
            .forceEnableSaveDialog()
            .build()

        return Credentials.getClient(context as Application, options)
    }


    @AuthScope
    @Provides
    fun providePhoneAuthProvider(auth: FirebaseAuth) = PhoneAuthProvider.getInstance(auth)

}