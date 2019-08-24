package com.example.flupic.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.flupic.BaseApplication
import com.example.flupic.MainActivity
import com.example.flupic.di.module.ActivityModule
import com.example.flupic.di.module.ApplicationModule
import com.example.flupic.ui.authenticationFragment
import com.example.flupic.ui.registrationFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton
import androidx.lifecycle.ViewModel
import com.example.flupic.ui.inside.*
import com.example.flupic.viewmodels.*
import dagger.MapKey
import dagger.multibindings.IntoMap
import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass


//Main app component
@Singleton
@Component(modules = [
AndroidSupportInjectionModule::class,
ActivityBuildersModule::class,
ApplicationModule::class,
ViewModelsFactoryModule::class
])
interface ApplicationComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
}


    //Connecting Activity to graph
@Module
abstract class ActivityBuildersModule{

    @ContributesAndroidInjector(
        modules = [FragmentBuildersModule::class, ActivityModule::class, ViewModulsModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}

        //Connecting Fragments to graph
@Module
abstract class FragmentBuildersModule{

            @ContributesAndroidInjector
            abstract fun contributeAuthentication(): authenticationFragment

            @ContributesAndroidInjector
            abstract  fun contributeRegistration(): registrationFragment

            @ContributesAndroidInjector
            abstract fun contributeInsideFragment(): insideFragment

            @ContributesAndroidInjector
            abstract fun contributeFeedFragment(): feedFragment

            @ContributesAndroidInjector
            abstract fun contributeEditFragment(): editFragment

            @ContributesAndroidInjector
            abstract fun contributeAddFragment(): addFragment

            @ContributesAndroidInjector
            abstract fun contributeAccountFragment(): accountFragment

            @ContributesAndroidInjector
            abstract fun contributeDetailFragment(): detailFragment

}

        //ViewModels and there Factory-----------------------------------------------------------------
@Module
abstract class ViewModelsFactoryModule{

    @Binds
    abstract fun bindViewModelsFactory(factory: ViewModelProviderFactory) : ViewModelProvider.Factory
}

            //ViewModels
@Module
abstract class ViewModulsModule {

                @Binds
                @IntoMap
                @ViewModelKey(EditViewModel::class)
                abstract fun bindEditViewModel(editViewModel: EditViewModel): ViewModel

                @Binds
                @IntoMap
                @ViewModelKey(AddViewModel::class)
                abstract fun bindAddViewModel(addViewModel: AddViewModel): ViewModel

                @Binds
                @IntoMap
                @ViewModelKey(ProfileViewModel::class)
                abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

                @Binds
                @IntoMap
                @ViewModelKey(DetailViewModel::class)
                abstract fun bindDetailViewModel(detailViewModel: DetailViewModel): ViewModel

                @Binds
                @IntoMap
                @ViewModelKey(FeedViewModel::class)
                abstract fun bindFeedViewModel(feedViewModel: FeedViewModel): ViewModel
            }
//-----------------------------------------------------------------------------------------------------



