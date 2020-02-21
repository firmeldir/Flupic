package com.example.flupic.scrobbler.model.firebase

import android.location.Location
import android.media.session.MediaController
import com.example.flupic.scrobbler.model.PointMedia
import com.example.flupic.scrobbler.model.SharingPoint
import com.google.firebase.firestore.DocumentId

data class FireSharingPoint(
    @DocumentId  val id: String = "",
    val state: FirePointState? = FirePointState(),
    val media: FirePointMedia? = FirePointMedia(),
    val location: FirePointLocation? = FirePointLocation()
){
    companion object {
        fun from(sharingPoint: SharingPoint) = FireSharingPoint(
            sharingPoint.id,
            FirePointState.from(sharingPoint.state),
            FirePointMedia.from(sharingPoint.media),
            FirePointLocation.from(sharingPoint.location))
    }
}