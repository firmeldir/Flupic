package com.example.flupic.scrobbler.model.firebase

import com.example.flupic.scrobbler.model.PointMedia

data class FirePointMedia(
    val title: String = "",
    val artist: String = "",
    val duration: Double = 0.0
){

    fun updatableFor(media: FirePointMedia) : Boolean = title != media.title || artist != media.artist

    companion object{
        fun from(media: PointMedia) = FirePointMedia(media.title, media.artist, media.duration)
    }
}