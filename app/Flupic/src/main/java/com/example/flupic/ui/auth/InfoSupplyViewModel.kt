package com.example.flupic.ui.auth

import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.flupic.R
import com.example.flupic.domain.auth.FinishUserRegistrationParameters
import com.example.flupic.domain.auth.FinishUserRegistrationUseCase
import com.example.flupic.result.Event
import com.example.flupic.result.Result
import com.example.flupic.util.PickPhotoViewModelDelegate
import com.example.flupic.util.exception.FirebaseAuthError
import com.example.flupic.util.exception.ResourceableException
import com.example.flupic.util.map
import com.example.flupic.util.view.SnackbarMessage
import java.lang.Exception
import javax.inject.Inject


class InfoSupplyViewModel @Inject constructor(
    pickPhotoViewModelDelegate: PickPhotoViewModelDelegate,
    private val finishUserRegistrationUseCase: FinishUserRegistrationUseCase
) : ViewModel(), PickPhotoViewModelDelegate by pickPhotoViewModelDelegate{

    companion object{
        private const val TAG = "TAG InfoSupplyViewModel"
    }

    private val _snackBarMessage = MutableLiveData<Event<SnackbarMessage>>()
    val snackbarMessage: LiveData<Event<SnackbarMessage>>
        get() = _snackBarMessage

    private val _navigateToProfileAction = MediatorLiveData<Event<Unit>>()
    val navigateToProfileAction: LiveData<Event<Unit>>
        get() = _navigateToProfileAction

    private val _onLoading = MediatorLiveData<Boolean>()
    val onLoading: LiveData<Boolean>
        get() = _onLoading

    private val finishUserRegistrationResult = MutableLiveData<Result<Unit>>()

    init {
        _onLoading.addSource(finishUserRegistrationResult){
            _onLoading.value = it is Result.Loading
        }

        _navigateToProfileAction.addSource(finishUserRegistrationResult){
            if(it is Result.Success)_navigateToProfileAction.value = Event(Unit)
            else if(it is Result.Error) handleError(it.exception)
        }
    }

    fun onPickPhotoClick(){
        emitPickPhotoRequest()
    }

    private fun handleError(e: Exception){
        if(e is ResourceableException){
            showSnackBarMessage(e.stringResourceId)
        }else{
            Log.e(TAG, e.message.toString())
            showSnackBarMessage(R.string.encapsulated_error)
        }
    }

    fun showSnackBarMessage(@StringRes stringResourceId: Int){
        _snackBarMessage.value = Event(
            SnackbarMessage(
                messageId = stringResourceId,
                longDuration = true
            )
        )
    }

    fun onNextClick(username: String){
        finishUserRegistrationUseCase(
            FinishUserRegistrationParameters(username, currentPickedPhoto.value),
            finishUserRegistrationResult,
            viewModelScope)
    }
}