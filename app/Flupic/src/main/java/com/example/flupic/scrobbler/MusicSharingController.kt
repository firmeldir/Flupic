package com.example.flupic.scrobbler

import android.location.Location
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.flupic.scrobbler.data.PeriodicLocationDataSource
import com.example.flupic.scrobbler.data.SharedMusicRepository
import com.example.flupic.scrobbler.data.SharedMusicUpdateParameters
import com.example.flupic.scrobbler.util.isActive
import kotlinx.coroutines.*
import javax.inject.Inject

class MusicSharingController @Inject constructor (
    private val sharedMusicRepository: SharedMusicRepository,
    private val periodicLocationDataSource: PeriodicLocationDataSource
) {

    private var currentLocationObservable: LiveData<Location?>? = null

    private val locationObserver = Observer<Location?> {
        postSharedMusicUpdate(SharedMusicUpdateParameters.from(currentController, currentLocationObservable?.value))
    }

    fun onDestroy(){
        setSharedMusicActivityState(false)

        currentLocationObservable?.removeObserver(locationObserver)
        periodicLocationDataSource.clearListener()
        currentController?.unregisterCallback(mediaControllerStateListener)

        coroutineJob.cancel()
    }

    fun onCreate(){
        periodicLocationDataSource.startListening()
        currentLocationObservable = periodicLocationDataSource.getObservableUserLocation()
        currentLocationObservable?.observeForever(locationObserver)
    }


    //   *   *   *     *   *   *     *   *   *

    private var currentController: MediaController? = null
        set(value){
            if(!isInvalidPackage(value)){
                field = value
                postSharedMusicUpdate(SharedMusicUpdateParameters.from(value, currentLocationObservable?.value))
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
                postSharedMusicUpdate(SharedMusicUpdateParameters.from(metadata))
            }

            override fun onPlaybackStateChanged(state: PlaybackState?) {
                super.onPlaybackStateChanged(state)
                if(state == null)return
                setSharedMusicActivityState(state.isActive())
            }
        }

    //    public static final long PLAYBACK_POSITION_UNKNOWN = -1L;
    //    public static final int STATE_BUFFERING = 6;
    //    public static final int STATE_CONNECTING = 8;
    //    public static final int STATE_ERROR = 7;
    //    public static final int STATE_FAST_FORWARDING = 4;
    //    public static final int STATE_NONE = 0;
    //    public static final int STATE_PAUSED = 2;
    //    public static final int STATE_PLAYING = 3;
    //    public static final int STATE_REWINDING = 5;
    //    public static final int STATE_SKIPPING_TO_NEXT = 10;
    //    public static final int STATE_SKIPPING_TO_PREVIOUS = 9;
    //    public static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
    //    public static final int STATE_STOPPED = 1;


    //   *   *   *     *   *   *     *   *   *

    private fun postSharedMusicUpdate(parameters: SharedMusicUpdateParameters?){
        if(parameters == null)return

        coroutineScope.launch {
            sharedMusicRepository.sendSharedMusicUpdate(parameters)
        }
    }

    private fun setSharedMusicActivityState(state: Boolean){
        GlobalScope.launch {
            sharedMusicRepository.setSharedMusicActivityState(state)
        }
    }

    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

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