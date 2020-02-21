package com.example.flupic.scrobbler.model.firebase

import android.location.Location
import com.example.flupic.scrobbler.model.PointLocation
import com.example.flupic.scrobbler.util.toGeoHashString
import com.example.flupic.scrobbler.util.toGeoPoint
import com.google.firebase.firestore.GeoPoint

data class FirePointLocation(
    val location: GeoPoint? = null,
    val hash: String? = null
){
    companion object{
        fun from(pointLocation: PointLocation) = FirePointLocation(pointLocation.location, pointLocation.hash)
    }
}