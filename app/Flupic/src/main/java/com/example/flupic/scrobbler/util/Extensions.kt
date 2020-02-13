package com.example.flupic.scrobbler.util

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.Service
import android.content.pm.PackageManager
import android.location.Location
import android.media.MediaMetadata
import android.media.session.PlaybackState
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.flupic.result.Event
import com.example.flupic.scrobbler.model.ControllerMusicMetadata
import com.example.flupic.scrobbler.model.toControllerMusicMetadata
import com.example.flupic.util.exception.LocationAccessType
import com.example.flupic.util.exception.RequestLocationAccessException
import com.google.firebase.firestore.GeoPoint
import java.lang.Exception

fun Service.checkLocationPermissionAccess(error: MutableLiveData<Event<Exception>>?) : Boolean{

    val permissionAccessCoarseLocationApproved = ActivityCompat
        .checkSelfPermission(this, ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

    val permissionAccessBackgroundLocationApproved = ActivityCompat
        .checkSelfPermission(this, ACCESS_BACKGROUND_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

    if(!permissionAccessCoarseLocationApproved){
        error?.postValue(Event(RequestLocationAccessException(LocationAccessType.ACCESS_COARSE_LOCATION)))
        Log.e("VLAD", "error?.postValue(Event(RequestLocationAccessException(LocationAccessType.ACCESS_COARSE_LOCATION)))")
        return false
    }

    if(!permissionAccessBackgroundLocationApproved){
        error?.postValue(Event(RequestLocationAccessException(LocationAccessType.ACCESS_BACKGROUND_LOCATION)))
        Log.e("VLAD", "error?.postValue(Event(RequestLocationAccessException(LocationAccessType.ACCESS_BACKGROUND_LOCATION)))")
        return true
    }

    return true
}

fun Location.toGeoPoint() = GeoPoint(this.latitude, this.longitude)

fun PlaybackState?.isActive() = this?.state != 1 && this?.state != 2 && this != null