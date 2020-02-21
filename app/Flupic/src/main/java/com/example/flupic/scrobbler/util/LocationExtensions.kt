package com.example.flupic.scrobbler.util

import android.location.Location
import com.google.firebase.firestore.GeoPoint
import org.imperiumlabs.geofirestore.GeoLocation
import org.imperiumlabs.geofirestore.core.GeoHash

fun Location.toGeoPoint() = GeoPoint(this.latitude, this.longitude)

fun Location.toGeoHashString() = GeoHash(GeoLocation(this.latitude, this.longitude)).geoHashString