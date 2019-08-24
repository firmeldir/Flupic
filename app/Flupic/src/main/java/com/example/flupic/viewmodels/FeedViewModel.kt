package com.example.flupic.viewmodels

import androidx.lifecycle.ViewModel
import com.example.flupic.repository.InsideRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedViewModel @Inject constructor(val insideRepository: InsideRepository) : ViewModel(){

    //Coroutines
    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)


    fun like(access: String){
        coroutineScope.launch {
            insideRepository.like(access)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }
}