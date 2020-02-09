package com.example.flupic

import android.app.Application
import com.example.flupic.di.AppComponent
import com.example.flupic.di.DaggerAppComponent


class BaseApplication : Application(){

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}