package com.example.flupic.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flupic.domain.auth.AuthenticationCompletedUseCase
import com.example.flupic.domain.auth.FullRegistrationCompletedUseCase
import com.example.flupic.domain.invoke
import com.example.flupic.result.Event
import com.example.flupic.result.Result
import com.example.flupic.ui.activity.LaunchDestination
import com.example.flupic.util.combine
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authenticationCompletedUseCase: AuthenticationCompletedUseCase,
    private val fullRegistrationCompletedUseCase: FullRegistrationCompletedUseCase
) : ViewModel(){ //TODO : Make Delegate because the same case in Launch ViewModel

    private val authenticationCompletedResult = MutableLiveData<Result<Boolean>>()
    private val fullRegistrationCompletedResult = MutableLiveData<Result<Boolean>>()
    val launchDestination: LiveData<Event<LaunchDestination>>

    fun finishAuthFlow(){
        authenticationCompletedUseCase(authenticationCompletedResult, viewModelScope)
        fullRegistrationCompletedUseCase(fullRegistrationCompletedResult, viewModelScope)
    }

    init {
        launchDestination = authenticationCompletedResult.combine(fullRegistrationCompletedResult){ signedIn, fullyRegd ->
            if(signedIn is Result.Success && fullyRegd is Result.Success){

                if(signedIn.data && fullyRegd.data)
                    Event(LaunchDestination.APPLICATION)
                else if(signedIn.data)
                    Event(LaunchDestination.FULLY_REGISTRATION)
                else
                    Event(LaunchDestination.AUTHENTICATION)

            }else Event(LaunchDestination.AUTHENTICATION)

        }
    }
}