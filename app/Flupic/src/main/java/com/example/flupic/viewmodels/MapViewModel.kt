package com.example.flupic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flupic.domain.FireUserLocation
import com.example.flupic.domain.UserInfoLocation
import com.example.flupic.repository.MapRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(val mapRepository: MapRepository) : ViewModel(){

    //Coroutines
    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    //BusinessLocations
    private val _businessLocations: MutableLiveData<List<UserInfoLocation>> = MutableLiveData()
    val businessLocations: LiveData<List<UserInfoLocation>>
        get() = _businessLocations

    //LastLocation
    private val _lastLocation: MutableLiveData<FireUserLocation> = MutableLiveData()
    val lastLocation: LiveData<FireUserLocation>
        get() = _lastLocation

    private fun getLastLocation(){
        coroutineScope.launch {
            mapRepository.lastKnownLocation()?.let {
                _lastLocation.value = it
            }
        }
    }

    init {
        getBusinessLocations()
        getLastLocation()
    }

    //LastLocation
    private fun getBusinessLocations(){
        coroutineScope.launch {
            mapRepository.businessLocation()?.let {
                _businessLocations.value = it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }
}