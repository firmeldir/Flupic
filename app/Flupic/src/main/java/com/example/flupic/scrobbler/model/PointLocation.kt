package com.example.flupic.scrobbler.model

import android.location.Location
import com.example.flupic.scrobbler.util.toGeoHashString
import com.example.flupic.scrobbler.util.toGeoPoint
import com.google.firebase.firestore.GeoPoint

data class PointLocation(
    val location: GeoPoint?,
    val hash: String?
){
    companion object{
        fun from(location: Location?) : PointLocation?{
            location ?: return null
            return PointLocation(location.toGeoPoint(), location.toGeoHashString())
        }
    }
}