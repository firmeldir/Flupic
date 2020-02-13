package com.example.flupic.scrobbler.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flupic.result.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicSharingServiceConnecter @Inject constructor(){

    val isScrobbling = MutableLiveData<Boolean>()

    val error =  MutableLiveData<Event<Exception>>()

}