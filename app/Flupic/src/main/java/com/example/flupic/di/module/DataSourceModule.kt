package com.example.flupic.di.module

import android.content.Context
import com.example.flupic.R
import com.example.flupic.data.AppDatabase
import com.example.flupic.data.auth.AuthUserDataSource
import com.example.flupic.data.auth.FirebaseAuthUserDataSource
import com.example.flupic.data.users.FirebaseUserManagementDataSource
import com.example.flupic.data.users.FirebaseUsersRepository
import com.example.flupic.data.users.UserManagementDataSource
import com.example.flupic.data.users.UsersRepository
import com.example.flupic.data.video.YoutubeDataApiDataSource
import com.example.flupic.data.video.YoutubeDataApiDataSourceImpl
import com.example.flupic.ui.youtube.VideoPlayerControllerDelegate
import com.example.flupic.ui.youtube.VideoPlayerControllerDelegateImpl
import com.example.flupic.util.ExternalStoragePickPhotoViewModelDelegate
import com.example.flupic.util.PickPhotoViewModelDelegate
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Singleton
    @Provides
    fun provideAuthUserDataSource(
        auth: FirebaseAuth
    ): AuthUserDataSource {
        return FirebaseAuthUserDataSource(auth)
    }

    @Singleton
    @Provides
    fun provideUserManagementDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): UserManagementDataSource {
        return FirebaseUserManagementDataSource(auth, firestore, storage)
    }

    @Singleton
    @Provides
    fun provideUsersRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        database: AppDatabase
    ): UsersRepository {
        return FirebaseUsersRepository(auth, firestore, database)
    }

    @Singleton
    @Provides
    fun provideYoutubeDataApiDataSource(
        youtubeDataApi: YouTube
    ): YoutubeDataApiDataSource {
        return YoutubeDataApiDataSourceImpl(youtubeDataApi)
    }

    @Singleton
    @Provides
    fun provideYoutubeDataApi(
        context: Context
    ): YouTube {
        val transport: HttpTransport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = GsonFactory()

        return YouTube.Builder(transport, jsonFactory, null)
            .setApplicationName(context.getString(R.string.app_name))
            .build()
    }

    @Provides
    fun providePickPhotoViewModelDelegate(): PickPhotoViewModelDelegate {
        return ExternalStoragePickPhotoViewModelDelegate()
    }

    @Provides
    fun provideVideoPlayerControllerDelegate(): VideoPlayerControllerDelegate {
        return VideoPlayerControllerDelegateImpl()
    }
}