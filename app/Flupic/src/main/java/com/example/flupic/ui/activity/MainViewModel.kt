package com.example.flupic.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.flupic.domain.auth.ObserveAuthUserUseCase
import com.example.flupic.result.Event
import com.example.flupic.result.Result
import javax.inject.Inject
import com.example.flupic.domain.execute

class MainViewModel @Inject constructor(
    observeUserAuthStateUseCase: ObserveAuthUserUseCase
) : ViewModel(){

    private val _startAuthenticationAction = MediatorLiveData<Event<Unit>>()
    val startAuthenticationAction: LiveData<Event<Unit>>
        get() = _startAuthenticationAction

    init {
        val authenticationState = observeUserAuthStateUseCase.observe()

        _startAuthenticationAction.addSource(authenticationState){

            if((it as? Result.Success)?.data?.isSignedIn() == false)
                _startAuthenticationAction.value = Event(Unit)
        }

        observeUserAuthStateUseCase.execute()
    }
}