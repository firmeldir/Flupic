package com.example.flupic.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flupic.TAG
import com.example.flupic.domain.FireUser
import com.example.flupic.repository.InsideRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.net.URI
import java.net.URL
import javax.inject.Inject


class EditViewModel @Inject constructor(val insideRepository: InsideRepository) : ViewModel(){

    //Coroutines
    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    //User
    private val _curUser: MutableLiveData<FireUser> = MutableLiveData()
    val curUser: LiveData<FireUser>
    get() = _curUser

    //Exit when Done
    private val _isDone: MutableLiveData<Boolean> = MutableLiveData()
    val isDone: LiveData<Boolean>
        get() = _isDone

    init {
        getUser()
        _isDone.value = false
    }

    private fun getUser(){
        coroutineScope.launch {
            insideRepository.getUser(_curUser)
        }
    }

    fun applyChanges(fullname: String, bio: String, phone_num: String, category: String, uri: Uri?){
        coroutineScope.launch {
            insideRepository.applyChanges(fullname, bio, phone_num, category, uri, _isDone)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }
}

