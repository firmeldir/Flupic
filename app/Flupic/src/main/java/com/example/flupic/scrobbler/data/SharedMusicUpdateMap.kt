package com.example.flupic.scrobbler.data

import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import android.util.Log
import com.example.flupic.scrobbler.model.ControllerMusicMetadata
import com.example.flupic.scrobbler.model.toControllerMusicMetadata
import com.example.flupic.scrobbler.util.isActive

class SharedMusicUpdateMap private constructor(val updateMap: Map<String, Any>){

    companion object{

        private const val TITLE = "title"
        private const val ARTIST = "artist"
        private const val DURATION = "duration"
        private const val POSITION = "position"
        private const val PLAYER = "player"
        private const val LOCATION = "l"
        private const val GEO_HASH = "g"
        private const val IS_ACTIVE= "isActive"

        fun from(mediaMetadata : MediaMetadata) : SharedMusicUpdateMap?{

            val title: String = try {
                mediaMetadata.getString(MediaMetadata.METADATA_KEY_TITLE)
            } catch (ignored: Exception) { "" }

            val artist: String = try {
                mediaMetadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: mediaMetadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)
            } catch (ignored: Exception) { "" }

            var duration: Double = try {
                mediaMetadata.getLong(MediaMetadata.METADATA_KEY_DURATION).toDouble()
            } catch (ignored: Exception) { 0.0 }

            if(duration > 1200000){ Log.i(ControllerMusicMetadata.TAG, "duration > 1200000") ; return null}else {
                duration /= 1000
            }

            return SharedMusicUpdateMap(
                mapOf(
                    TITLE to title,
                    ARTIST to artist,
                    DURATION to duration
                ))
        }

        fun from(mediaController : MediaController) : SharedMusicUpdateMap?{
            val metadata = mediaController.metadata ?: return null
            val playbackState = mediaController.playbackState

            val title: String = try {
                metadata.getString(MediaMetadata.METADATA_KEY_TITLE)
            } catch (ignored: Exception) { "" }

            val artist: String = try {
                metadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: metadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)
            } catch (ignored: Exception) { "" }

            var duration: Double = try {
                metadata.getLong(MediaMetadata.METADATA_KEY_DURATION).toDouble()
            } catch (ignored: Exception) { 0.0 }

            if(duration > 1200000){ Log.i(ControllerMusicMetadata.TAG, "duration > 1200000") ; return null}else {
                duration /= 1000
            }

            val position = if (duration == 0.0 || playbackState == null) -1L else playbackState.position

            val player = mediaController.packageName

            val isActive = playbackState.isActive()

            return SharedMusicUpdateMap(
                mapOf(
                    TITLE to title,
                    ARTIST to artist,
                    DURATION to duration,
                    POSITION to position,
                    PLAYER to player,
                    IS_ACTIVE to isActive
                ))
        }

        fun from(metadata: ControllerMusicMetadata) =
            SharedMusicUpdateMap(
                mapOf(
                    TITLE to metadata.title,
                    ARTIST to metadata.artist,
                    DURATION to metadata.duration,
                    POSITION to metadata.position,
                    PLAYER to metadata.player,
                    IS_ACTIVE to metadata.isActive
                ))
    }

    fun updatableFrom(metadata: MediaMetadata?) : Boolean {
        if(metadata == null)return true

        val title: String = try {
            metadata.getString(MediaMetadata.METADATA_KEY_TITLE)
        } catch (ignored: java.lang.Exception) { "" }

        val artist: String = try {
            metadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: metadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)
        } catch (ignored: java.lang.Exception) { "" }

        return title != updateMap[TITLE] || artist != updateMap[ARTIST]
     }
}