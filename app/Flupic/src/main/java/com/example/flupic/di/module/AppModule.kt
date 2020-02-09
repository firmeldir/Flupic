package com.example.flupic.di.module

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.flupic.BaseApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun providesConnectivityManager(context: Context): ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

    @Provides
    fun providesApplication(context: Context): Application = context as Application

}