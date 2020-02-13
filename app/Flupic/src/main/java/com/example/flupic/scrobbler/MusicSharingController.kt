package com.example.flupic.scrobbler

import android.location.Location
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.flupic.scrobbler.data.PeriodicLocationDataSource
import com.example.flupic.scrobbler.data.SharedMusicRepository
import com.example.flupic.scrobbler.data.SharedMusicUpdateMap
import com.example.flupic.scrobbler.model.ControllerMusicMetadata
import com.example.flupic.scrobbler.model.toControllerMusicMetadata
import com.example.flupic.scrobbler.util.isActive
import kotlinx.coroutines.*
import javax.inject.Inject

class MusicSharingController @Inject constructor (
    private val sharedMusicRepository: SharedMusicRepository,
    private val periodicLocationDataSource: PeriodicLocationDataSource
) {

    private lateinit var currentLocationObservable: LiveData<Location?>

    private val locationObserver = Observer<Location?> {
        if(it == null)return@Observer

        val metadata = currentController?.toControllerMusicMetadata()
        if(metadata?.isActive != true)return@Observer

        postSharedMusicUpdate(metadata, it)
    }

    fun onDestroy(){
        setSharedMusicActivityState(false)

        currentLocationObservable.removeObserver(locationObserver)
        periodicLocationDataSource.clearListener()
        currentController?.unregisterCallback(mediaControllerStateListener)

        coroutineJob.cancel()
    }

    fun onCreate(){
        periodicLocationDataSource.startListening()
        currentLocationObservable = periodicLocationDataSource.getObservableUserLocation()
        currentLocationObservable.observeForever(locationObserver)
    }


    //   *   *   *     *   *   *     *   *   *

    private var latestPostedMusicUpdate: SharedMusicUpdateMap? = null

    private var currentController: MediaController? = null
        set(value){
            if(!isInvalidPackage(value)){
                field = value
                postSharedMusicUpdate(value?.toControllerMusicMetadata())
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
                if(latestPostedMusicUpdate?.updatableFrom(metadata) != false){
                    postSharedMusicUpdate(metadata)
                }
            }

            override fun onPlaybackStateChanged(state: PlaybackState?) {
                super.onPlaybackStateChanged(state)
                if(!state.isActive()){
                    setSharedMusicActivityState(false)
                }
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

    private fun postSharedMusicUpdate(metadata: ControllerMusicMetadata?, location: Location){
        if(metadata == null)return

        val updateMap = SharedMusicUpdateMap.from(metadata) ?: return
        latestPostedMusicUpdate = updateMap

        coroutineScope.launch {
            sharedMusicRepository.sendSharedMusicUpdate(metadata, location)
        }
    }

    private fun postSharedMusicUpdate(metadata: ControllerMusicMetadata?){
        if(metadata == null)return

        val updateMap = SharedMusicUpdateMap.from(metadata) ?: return
        latestPostedMusicUpdate = updateMap

        coroutineScope.launch {
            sharedMusicRepository.sendSharedMusicUpdate(updateMap)
        }
    }

    private fun postSharedMusicUpdate(metadata: MediaMetadata?){
        if(metadata == null)return

        val updateMap = SharedMusicUpdateMap.from(metadata) ?: return
        latestPostedMusicUpdate = updateMap

        coroutineScope.launch {
            sharedMusicRepository.sendSharedMusicUpdate(updateMap)
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