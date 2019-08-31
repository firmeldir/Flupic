package com.example.flupic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flupic.domain.FlupicDish
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

    private val _feed: MutableLiveData<List<FlupicDish>> = MutableLiveData()
    val feed: LiveData<List<FlupicDish>>
        get() = _feed


    init {
        loadFeed()
    }

    private fun loadFeed(){
        coroutineScope.launch{
            _feed.value = insideRepository.loadFeed()
        }
    }


    fun like(accessId: String, authorId: String){
        coroutineScope.launch {
            insideRepository.like(accessId, authorId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }
}