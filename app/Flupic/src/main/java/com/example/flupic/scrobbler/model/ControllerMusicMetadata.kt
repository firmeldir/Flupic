package com.example.flupic.scrobbler.model

import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import android.util.Log
import com.example.flupic.scrobbler.model.ControllerMusicMetadata.Companion.TAG
import com.example.flupic.scrobbler.util.isActive
import kotlinx.coroutines.NonCancellable.isActive


data class ControllerMusicMetadata(
    val title: String,
    val artist: String,

    val duration: Double,
    val position: Long,
    val player: String,

    val isActive: Boolean = false
){
    companion object{
        const val TAG = "TAG ControllerMusicMeta"
    }
}

fun MediaController.toControllerMusicMetadata() : ControllerMusicMetadata?{
    val metadata = this.metadata ?: return null
    val playbackState = this.playbackState

    val title: String = try {
        metadata.getString(MediaMetadata.METADATA_KEY_TITLE)
    } catch (ignored: Exception) { "" }

    val artist: String = try {
        metadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: metadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)
    } catch (ignored: Exception) { "" }

    var duration: Double = try {
        metadata.getLong(MediaMetadata.METADATA_KEY_DURATION).toDouble()
    } catch (ignored: Exception) { 0.0 }

    if(duration > 1200000){ Log.i(TAG, "duration > 1200000") ; return null}else {
        duration /= 1000
    }

    val position = if (duration == 0.0 || playbackState == null) -1L else playbackState.position

    val player = this.packageName

    val isActive = playbackState.isActive()

    return ControllerMusicMetadata(title, artist, duration, position, player, isActive)
}
