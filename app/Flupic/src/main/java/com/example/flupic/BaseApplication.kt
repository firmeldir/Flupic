package com.example.flupic

import com.example.flupic.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BaseApplication : DaggerApplication(){

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerApplicationComponent.builder()
        .application(this).build()
}