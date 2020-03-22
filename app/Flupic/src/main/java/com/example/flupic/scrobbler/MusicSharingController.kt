package com.example.flupic.scrobbler

import android.location.Location
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.flupic.scrobbler.data.PeriodicLocationDataSource
import com.example.flupic.scrobbler.domain.*
import com.example.flupic.scrobbler.model.PointLocation
import com.example.flupic.scrobbler.model.PointMedia
import com.example.flupic.scrobbler.model.PointState
import com.example.flupic.scrobbler.model.SharingPoint
import kotlinx.coroutines.*
import javax.inject.Inject

class MusicSharingController @Inject constructor (
    private val activateSharingPointUseCase: ActivateSharingPointUseCase,
    private val sendSharingPointUpdateUseCase: SendSharingPointUpdateUseCase,
    private val updateSharingPointMediaUseCase: UpdateSharingPointMediaUseCase,
    private val updateSharingPointStateUseCase: UpdateSharingPointStateUseCase,
    private val periodicLocationDataSource: PeriodicLocationDataSource
) {

    private var currentLocationObservable: LiveData<Location?>? = null

    private val locationObserver = Observer<Location?> {

        val location = PointLocation.from(it) ?: return@Observer
        val state = PointState.from(currentController?.playbackState) ?: return@Observer

        sendSharingPointUpdateUseCase(SendSharingPointUpdateParameters(state, location), coroutineScope)
    }

    fun onDestroy(){
        updateSharingPointStateUseCase(PointState(false, 0L))

        currentLocationObservable?.removeObserver(locationObserver)
        periodicLocationDataSource.clearListener()
        currentController?.unregisterCallback(mediaControllerStateListener)

        coroutineJob.cancel()
    }

    fun onCreate(){
        coroutineJob = Job()
        coroutineScope =  CoroutineScope(coroutineJob + Dispatchers.Main)

        periodicLocationDataSource.startListening()
        currentLocationObservable = periodicLocationDataSource.getObservableUserLocation()
        currentLocationObservable?.observeForever(locationObserver)
    }


    //   *   *   *     *   *   *     *   *   *

    private var currentController: MediaController? = null
        set(value){
            if(!isInvalidPackage(value)){
                field = value
                activateSharingPoint(value)
            }
        }

    fun updateMediaControllers(controllers: List<MediaController>){
        for(controller in controllers){
            if(controller.playbackState?.state == PlaybackState.STATE_PLAYING){
                currentController = controller
                updateMediaControllerStateListener(controllers)
                return
            }
        }

        currentController = controllers[0]
        updateMediaControllerStateListener(controllers)
    }

    private fun updateMediaControllerStateListener(controllers: List<MediaController>){
        val cController = currentController ?: return

        for (controller in controllers) controller.unregisterCallback(mediaControllerStateListener)

        cController.registerCallback(mediaControllerStateListener)
    }


    private var mediaControllerStateListener: MediaController.Callback =
        object : MediaController.Callback() {

            override fun onMetadataChanged(metadata: MediaMetadata?) {
                val media = PointMedia.from(metadata) ?: return

                updateSharingPointMediaUseCase(media, coroutineScope)

            }

            override fun onPlaybackStateChanged(state: PlaybackState?) {
                super.onPlaybackStateChanged(state)
                val pointState = PointState.from(state) ?: return

                updateSharingPointStateUseCase(pointState, coroutineScope)
            }
        }



    private lateinit var coroutineJob: Job
    private lateinit var coroutineScope: CoroutineScope

    private fun activateSharingPoint(currentController: MediaController?){
        val point = SharingPoint.from(currentController, currentLocationObservable?.value) ?: return
        activateSharingPointUseCase(point, coroutineScope)
    }

    companion object{

        /*
        *  Means invalid to scrobble app that use media controller
        */

        fun isInvalidPackage(controller: MediaController?): Boolean{
            val playerPackageName = controller?.packageName ?: return true
            return (playerPackageName.contains(".chrome") || playerPackageName.contains("firefox") ||
                    playerBlacklist.contains(playerPackageName))
        }


        // TODO : Add this app player

        private val playerBlacklist = listOf(
            "au.com.shiftyjelly.pocketcasts", "com.bambuna.podcastaddict", "tunein.player, sanity.freeaudiobooks",
            "com.audible.application", "sanity.podcast.freak", "com.samsung.android.video", "tv.twitch.android.app",
            "tv.molotov.app", "com.netflix.mediaclient", "com.android.server.telecom", "tunein.player", "radiotime.player")

    }
}