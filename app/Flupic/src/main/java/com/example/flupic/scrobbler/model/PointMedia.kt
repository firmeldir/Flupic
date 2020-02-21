package com.example.flupic.scrobbler.model

import android.media.MediaMetadata
import android.util.Log
import com.example.flupic.scrobbler.model.firebase.FirePointMedia

data class PointMedia(
    val title: String,
    val artist: String,
    val duration: Double
){
    companion object{
        private const val TAG = "TAG PointMedia"
        private const val TRACK_MAX_LENGTH = 1200

        fun from(metadata: MediaMetadata?) : PointMedia?{
            metadata ?: return null

            val title: String = try {
                metadata.getString(MediaMetadata.METADATA_KEY_TITLE)
            } catch (ignored: Exception) { "" }

            val artist: String = try {
                metadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: metadata.getString(
                    MediaMetadata.METADATA_KEY_ALBUM_ARTIST)
            } catch (ignored: Exception) { "" }

            val duration: Double = try {
                metadata.getLong(MediaMetadata.METADATA_KEY_DURATION).toDouble() / 1000
            } catch (ignored: Exception) { 0.0 }

            if(duration > TRACK_MAX_LENGTH){
                Log.e(TAG, "duration > $TRACK_MAX_LENGTH")
                return null
            }

            return PointMedia(title, artist, duration)
        }
    }
}