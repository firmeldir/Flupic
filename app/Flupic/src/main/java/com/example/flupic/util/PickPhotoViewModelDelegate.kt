package com.example.flupic.util

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flupic.result.ActivityRequestEvent
import javax.inject.Inject

typealias PickPhotoRequest = ActivityRequestEvent<PickPhotoRequestHandler>

interface PickPhotoViewModelDelegate {

    val currentPickedPhoto: LiveData<Uri>

    val performPickPhotoEvent: MutableLiveData<PickPhotoRequest>

    fun emitPickPhotoRequest()

    fun onPickPhotoResult(requestCode: Int, resultCode: Int, data: Intent?)
}

class ExternalStoragePickPhotoViewModelDelegate @Inject constructor() : PickPhotoViewModelDelegate{

    private val _currentPickedPhoto =  MutableLiveData<Uri>()
    override val currentPickedPhoto: LiveData<Uri>
        get() = _currentPickedPhoto

    override val performPickPhotoEvent = MutableLiveData<PickPhotoRequest>()

    override fun emitPickPhotoRequest() {
        performPickPhotoEvent.postValue(PickPhotoRequest(PickPhotoRequestHandler))
    }

    override fun onPickPhotoResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = PickPhotoRequestHandler.onResult(requestCode, resultCode, data)?.data ?: return
        _currentPickedPhoto.value = result
    }
}

