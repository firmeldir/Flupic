package com.example.flupic.scrobbler.data

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import javax.inject.Inject

interface PeriodicLocationDataSource {

    fun startListening()

    fun getObservableUserLocation(): LiveData<Location?>

    /**
     * Call this method to clear listeners to avoid leaks.
     */
    fun clearListener()
}

class PeriodicLocationDataSourceImpl @Inject constructor(
    private val locationProvider : FusedLocationProviderClient
) : PeriodicLocationDataSource{

    companion object{
        private const val REQUESTING_INTERVAL = 10 * 1000L
    }

    private val currentLocationObservable = MutableLiveData<Location?>()

    private var isAlreadyListening = false



    private val newRequestInstance
        get() = LocationRequest().apply {
            interval = REQUESTING_INTERVAL; priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    private var propagator: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            Log.i("VLAD", "------")
            currentLocationObservable.postValue(locationResult?.lastLocation)
        }
    }


    override fun startListening() {
        if (!isAlreadyListening) {
            locationProvider.requestLocationUpdates(newRequestInstance, propagator, null)
            isAlreadyListening = true
        }
    }

    override fun getObservableUserLocation(): LiveData<Location?> = currentLocationObservable

    override fun clearListener() {
        locationProvider.removeLocationUpdates(propagator)
    }
}