package com.example.flupic.scrobbler.model

import android.location.Location
import android.media.session.MediaController

data class SharingPoint(
    val id: String,
    val state: PointState,
    val media: PointMedia,
    val location: PointLocation
){
    companion object{
        fun from(controller: MediaController?, location: Location?): SharingPoint?{
            controller ?: return null

            val state = PointState.from(controller.playbackState) ?: return null

            val media = PointMedia.from(controller.metadata) ?: return null

            val point = PointLocation.from(location) ?: PointLocation.from(Location(""))!!

            return SharingPoint(
                state = state,
                media = media,
                location = point,

                id = ""
            )
        }
    }
}