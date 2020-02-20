package com.example.flupic.di.viewmodel

import androidx.lifecycle.ViewModel
import com.example.flupic.di.subcomponent.AuthScope
import com.example.flupic.ui.activity.LaunchActivityViewModel
import com.example.flupic.ui.activity.MainViewModel
import com.example.flupic.ui.auth.InfoSupplyViewModel
import com.example.flupic.ui.profile.UserProfileViewModel
import com.example.flupic.ui.youtube.YoutubeViewModel
import com.example.flupic.util.handler.PhoneNumberVerificationHandler
import com.example.flupic.util.handler.PhoneProviderResponseHandler
import com.example.flupic.util.handler.SmartLockHandler
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class AppViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LaunchActivityViewModel::class)
    internal abstract fun bindLaunchActivityViewModel(viewmodel: LaunchActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewmodel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InfoSupplyViewModel::class)
    internal abstract fun bindInfoSupplyViewModel(viewmodel: InfoSupplyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    internal abstract fun bindUserProfileViewModel(viewmodel: UserProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(YoutubeViewModel::class)
    internal abstract fun bindYoutubeViewModel(viewmodel: YoutubeViewModel): ViewModel
}
