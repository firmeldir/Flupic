package com.example.flupic.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.flupic.BaseApplication
import com.example.flupic.MainActivity
import com.example.flupic.di.module.ActivityModule
import com.example.flupic.di.module.ApplicationModule
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [
AndroidSupportInjectionModule::class,

ActivityBuildersModule::class,

ApplicationModule::class,

ViewModelsFactoryModule::class ])
interface ApplicationComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
}




@Module
abstract class ActivityBuildersModule{

    @ContributesAndroidInjector(
        modules = [FragmentBuildersModule::class, ActivityModule::class, ViewModulsModule::class])
    abstract fun contributeMainActivity(): MainActivity
}

@Module
abstract class FragmentBuildersModule



@Module
abstract class ViewModulsModule



@Module
abstract class ViewModelsFactoryModule{

    @Binds
    abstract fun bindViewModelsFactory(factory: ViewModelProviderFactory) : ViewModelProvider.Factory
}










//@Binds
//@IntoMap
//@ViewModelKey(EditViewModel::class)
//abstract fun bindEditViewModel(editViewModel: EditViewModel): ViewModel

//            @ContributesAndroidInjector
//            abstract fun contributeFeedFragment(): feedFragment
//
//            @ContributesAndroidInjector(modules = [MapModule::class])
//            abstract fun contributeEditFragment(): editFragment


