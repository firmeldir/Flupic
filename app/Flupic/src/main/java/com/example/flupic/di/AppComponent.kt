package com.example.flupic.di

import android.content.Context
import com.example.flupic.di.module.AppModule
import com.example.flupic.di.module.DataSourceModule
import com.example.flupic.di.module.FirebaseModule
import com.example.flupic.di.subcomponent.AppSubcomponents
import com.example.flupic.di.subcomponent.AuthComponent
import com.example.flupic.di.viewmodel.AppViewModelModule
import com.example.flupic.di.viewmodel.ViewModelFactoryModule
import com.example.flupic.ui.activity.LaunchActivity
import com.example.flupic.ui.activity.MainActivity
import com.example.flupic.ui.auth.InfoSupplyFragment
import com.example.flupic.ui.profile.UserProfileFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    FirebaseModule::class,
    DataSourceModule::class,
    AppModule::class,
    AppViewModelModule::class,
    AppSubcomponents::class,
    ViewModelFactoryModule::class
])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: LaunchActivity)

    fun inject(fragment: InfoSupplyFragment)
    fun inject(fragment: UserProfileFragment)

    fun authComponent(): AuthComponent.Factory


    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

}