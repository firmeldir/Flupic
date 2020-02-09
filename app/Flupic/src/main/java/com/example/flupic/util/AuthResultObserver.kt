package com.example.flupic.util

import androidx.lifecycle.Observer
import com.example.flupic.result.Event
import com.example.flupic.result.Result

abstract class AuthResultObserver<T>(private val progressView: ProgressView?) : Observer<Event<Result<T>>> {

    override fun onChanged(t: Event<Result<T>>?) {

        if(t?.peekContent() is Result.Loading){ progressView?.showProgress(); return }

        progressView?.hideProgress()

        when(val result = t?.getContentIfNotHandled() ?: return){
            is Result.Success-> onSuccess(result.data)

            is Result.Error-> onFailure(result.exception)
        }
    }

    protected abstract fun onSuccess(t: T)

    protected abstract fun onFailure(e: Exception)
}

interface ProgressView{
    fun showProgress()
    fun hideProgress()
}