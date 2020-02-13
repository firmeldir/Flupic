package com.example.flupic.scrobbler.data

import android.location.Location
import android.media.MediaMetadata
import android.media.session.MediaController
import android.util.Log
import com.example.flupic.scrobbler.util.isActive
import com.example.flupic.scrobbler.util.toGeoHashString
import com.example.flupic.scrobbler.util.toGeoPoint
import java.io.Serializable

class SharedMusicUpdateParameters private constructor(val updateMap: Map<String, Any>){

    companion object{

        private const val TAG = "TAG SMUpdateMap"

        private const val TITLE = "title"
        private const val ARTIST = "artist"
        private const val DURATION = "duration"
        private const val POSITION = "position"
        private const val PLAYER = "player"
        private const val LOCATION = "l"
        private const val GEO_HASH = "g"
        private const val IS_ACTIVE= "isActive"

        fun from(mediaMetadata : MediaMetadata?) : SharedMusicUpdateParameters?{
            if(mediaMetadata == null)return null

            val title: String = try {
                mediaMetadata.getString(MediaMetadata.METADATA_KEY_TITLE)
            } catch (ignored: Exception) { "" }

            val artist: String = try {
                mediaMetadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: mediaMetadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)
            } catch (ignored: Exception) { "" }

            var duration: Double = try {
                mediaMetadata.getLong(MediaMetadata.METADATA_KEY_DURATION).toDouble()
            } catch (ignored: Exception) { 0.0 }

            if(duration > 1200000){ Log.i(TAG, "duration > 1200000") ; return null}else {
                duration /= 1000
            }

            return SharedMusicUpdateParameters(
                mapOf(
                    TITLE to title,
                    ARTIST to artist,
                    DURATION to duration
                ))
        }

        fun from(mediaController : MediaController?, location: Location?) : SharedMusicUpdateParameters?{
            if(mediaController == null || location == null)return null

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

            if(duration > 1200000){ Log.i(TAG, "duration > 1200000") ; return null}else {
                duration /= 1000
            }

            val position = if (duration == 0.0 || playbackState == null) -1L else playbackState.position

            val player = mediaController.packageName

            val map = mutableMapOf<String, Any>(
                TITLE to title,
                ARTIST to artist,
                DURATION to duration,
                POSITION to position,
                PLAYER to player,
                LOCATION to location.toGeoPoint(),
                GEO_HASH to location.toGeoHashString()
            )

            if(playbackState != null){
                map[IS_ACTIVE] = playbackState.isActive()
            }

            return SharedMusicUpdateParameters(map)
        }
    }

    fun updatableFrom(parameters: SharedMusicUpdateParameters) : Boolean {
        val map = parameters.updateMap

        return map[TITLE] != updateMap[TITLE] || map[ARTIST] != updateMap[ARTIST] ||
                map[GEO_HASH] != updateMap[GEO_HASH] || map[LOCATION] != updateMap[LOCATION]
    }
}