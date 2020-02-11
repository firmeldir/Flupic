package com.example.flupic.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flupic.R
import com.example.flupic.domain.invoke
import com.example.flupic.domain.users.ForceUpdateUserUseCase
import com.example.flupic.domain.users.GetUserUseCase
import com.example.flupic.model.User
import com.example.flupic.result.Event
import com.example.flupic.result.Result
import com.example.flupic.util.combine
import com.example.flupic.util.map
import com.example.flupic.util.uniteSuccessResults
import com.example.flupic.util.view.SnackbarMessage
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val forceUpdateUserUseCase: ForceUpdateUserUseCase
) : ViewModel(){

    val isLoading: LiveData<Boolean>

    val swipeRefreshing: LiveData<Boolean>

    private val _snackBarMessage = MediatorLiveData<Event<SnackbarMessage>>()
    val snackBarMessage: LiveData<Event<SnackbarMessage>>
        get() = _snackBarMessage


    private val getUserResult = MediatorLiveData<Result<User>>()

    private val forceUpdateUserResult = MediatorLiveData<Result<User>>()

    val userProfile: LiveData<User>


    init {

        swipeRefreshing = forceUpdateUserResult.map {
            // Whenever refresh finishes, stop the indicator, whatever the result
            false
        }

        isLoading = getUserResult.map{ it == Result.Loading }

        _snackBarMessage.addSource(getUserResult) {
            if (it is Result.Error) { _snackBarMessage.postValue(
                Event(
                    SnackbarMessage( messageId = R.string.error_while_uploading_profile, longDuration = true)
                )
            )
            }
        }

        _snackBarMessage.addSource(forceUpdateUserResult) {
            if (it is Result.Error) { _snackBarMessage.postValue(
                Event(
                    SnackbarMessage( messageId = R.string.error_while_updating_profile, longDuration = true)
                )
            )
            }
        }

        userProfile = getUserResult.uniteSuccessResults(forceUpdateUserResult)
        getUserUseCase(getUserResult, viewModelScope)
    }

    fun onSwipeRefresh(){
        forceUpdateUserUseCase(forceUpdateUserResult, viewModelScope)
    }
}