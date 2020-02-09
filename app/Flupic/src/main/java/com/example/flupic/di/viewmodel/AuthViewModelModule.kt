package com.example.flupic.di.viewmodel

import androidx.lifecycle.ViewModel
import com.example.flupic.di.subcomponent.AuthScope
import com.example.flupic.ui.auth.AuthViewModel
import com.example.flupic.util.handler.PhoneNumberVerificationHandler
import com.example.flupic.util.handler.PhoneProviderResponseHandler
import com.example.flupic.util.handler.SmartLockHandler
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class AuthViewModelModule {

    @AuthScope
    @Binds
    @IntoMap
    @ViewModelKey(PhoneNumberVerificationHandler::class)
    internal abstract fun bindPhoneNumberVerificationHandler(viewmodel: PhoneNumberVerificationHandler): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PhoneProviderResponseHandler::class)
    internal abstract fun bindPhoneProviderResponseHandler(viewmodel: PhoneProviderResponseHandler): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SmartLockHandler::class)
    internal abstract fun bindSmartLockHandler(viewmodel: SmartLockHandler): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    internal abstract fun bindAuthViewModel(viewmodel: AuthViewModel): ViewModel
}


