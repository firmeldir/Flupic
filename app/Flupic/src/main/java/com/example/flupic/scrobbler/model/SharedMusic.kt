package com.example.flupic.scrobbler.model

import android.location.Location
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint
import org.imperiumlabs.geofirestore.core.GeoHash

data class SharedMusic(
    @DocumentId val id: String,
    val title: String,
    val artist: String,

    val duration: Double, // = 0.0
    val position: Long, // = 0L
    val player: String,

    val l: GeoPoint? = null,
    val g: String? = null,

    val isActive: Boolean = false
)

data class FireSharedMusic(
    val title: String = "",
    val artist: String = "",

    val duration: Double = 0.0, //
    val position: Long = 0L, //
    val player: String = "",

    var l: GeoPoint? = null,
    var g: String? = null,

    val isActive: Boolean = true,
    @DocumentId val id: String = ""
){
    companion object{

        fun fromControllerMusicMetadata(metadata: ControllerMusicMetadata, geoPoint: GeoPoint, geoHash: String) : FireSharedMusic
            = FireSharedMusic(
            title = metadata.title,
            artist = metadata.artist,
            duration = metadata.duration,
            position = metadata.position,
            player = metadata.player,
            l = geoPoint,
            g = geoHash,
            isActive = metadata.isActive
        )
    }
}

