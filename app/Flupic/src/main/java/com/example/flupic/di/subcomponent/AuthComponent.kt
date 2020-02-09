package com.example.flupic.di.subcomponent

import com.example.flupic.di.module.AuthModule
import com.example.flupic.di.viewmodel.AuthViewModelModule
import com.example.flupic.ui.auth.*
import dagger.Subcomponent


@AuthScope
@Subcomponent(modules = [AuthViewModelModule::class, AuthModule::class])
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthComponent
    }

    fun inject(fragment: AuthFragment)
    fun inject(fragment: StartFragment)
    fun inject(fragment: SignInFragment)
    fun inject(fragment: ConfirmationFragment)
    fun inject(fragment: InfoSupplyFragment)
}