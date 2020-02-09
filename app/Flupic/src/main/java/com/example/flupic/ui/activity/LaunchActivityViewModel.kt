package com.example.flupic.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flupic.domain.auth.AuthenticationCompletedUseCase
import com.example.flupic.domain.auth.FullRegistrationCompletedUseCase
import com.example.flupic.domain.invoke
import com.example.flupic.result.Event
import com.example.flupic.result.Result
import com.example.flupic.util.combine
import com.example.flupic.util.map
import javax.inject.Inject

class LaunchActivityViewModel @Inject constructor(
    authenticationCompletedUseCase: AuthenticationCompletedUseCase,
    fullRegistrationCompletedUseCase: FullRegistrationCompletedUseCase
) : ViewModel(){

    private val authenticationCompletedResult = MutableLiveData<Result<Boolean>>()
    private val fullRegistrationCompletedResult = MutableLiveData<Result<Boolean>>()
    val launchDestination: LiveData<Event<LaunchDestination>>

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

        authenticationCompletedUseCase(authenticationCompletedResult, viewModelScope)
        fullRegistrationCompletedUseCase(fullRegistrationCompletedResult, viewModelScope)
    }
}

enum class LaunchDestination {
    AUTHENTICATION,
    FULLY_REGISTRATION,
    APPLICATION
}