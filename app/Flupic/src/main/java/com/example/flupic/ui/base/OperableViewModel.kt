package com.example.flupic.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flupic.result.Event
import com.example.flupic.result.Result
import java.lang.Exception


open class OperableViewModel<R> constructor(application: Application): AndroidViewModel(application) {

    private val _operationResult: MutableLiveData<Event<Result<R>>> = MutableLiveData()
    val operationResult: LiveData<Event<Result<R>>>
        get() = _operationResult

    protected fun setOperationResult(result: R){
        _operationResult.value = Event(Result.Success(result))
    }

    protected fun setOperationResult(exception: Exception){
        _operationResult.value = Event(Result.Error(exception))
    }

    protected fun onOperationLoading(){
        _operationResult.value = Event(Result.Loading)
    }
}
