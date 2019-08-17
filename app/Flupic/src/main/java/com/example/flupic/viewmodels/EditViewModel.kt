package com.example.flupic.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flupic.TAG
import com.example.flupic.domain.FireUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.net.URI
import java.net.URL


class EditViewModel : ViewModel(){

    //Coroutines
    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    //Firebase
    private val auth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("users").document(auth.uid.toString())

    private val storage = FirebaseStorage.getInstance()
    private var storageRef = storage.reference

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
            _getUser(_curUser, ref)
        }
    }

    fun applyChanges(fullname: String, bio: String, phone_num: String, category: String, uri: Uri?){
        coroutineScope.launch {
            _applyChanges(_isDone,fullname, bio, phone_num, category,uri)
        }
    }

    suspend fun saveProfilePhoto(uri: Uri?){
        uri?.let {

            val imageRef = storageRef.child("users/${auth.uid}/avatar.jpg")

            try {
                imageRef.delete().await()
            }catch (e: Exception){
                Log.i(TAG, e.message)
            }

            var downloadURL : Uri? = null

            try {
                val task = imageRef.putFile(uri).continueWithTask{
                    if (!it.isSuccessful){
                        throw it.exception!!
                    }
                    return@continueWithTask imageRef.downloadUrl
                }

                downloadURL = task.await()

            }catch (e: Exception){
                Log.i(TAG, e.message)
            }

            try {

                ref.update(mapOf(
                    "photo_url" to downloadURL.toString()
                )).await()

            }catch (e: Exception){
                Log.i(TAG, e.message)
            }
        }
    }

    //todo in repo
    private suspend fun _applyChanges(_isDone: MutableLiveData<Boolean>, fullname: String, bio: String, phone_num: String, category: String, uri: Uri?){
        try {
            withContext(Dispatchers.IO){
                ref.update(mapOf(
                    "fullname" to fullname,
                    "phone_num" to phone_num,
                    "bio" to bio,
                    "category" to category
                )).await()

                saveProfilePhoto(uri)
            }
            _isDone.value = true
        }catch (e: Exception){
            Log.e(TAG, e.message)
            _isDone.value = true
        }
    }

    //todo in repo
    private suspend fun _getUser(_curUser: MutableLiveData<FireUser>, ref: DocumentReference){

        var userSnapshot: DocumentSnapshot? = null

        try {
            withContext(Dispatchers.IO){
                userSnapshot = ref.get().await()
            }

            userSnapshot?.let {
                _curUser.value = userSnapshot?.toObject(FireUser::class.java)
            }

        }catch (e: Exception){
            Log.e(TAG, e.message)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }
}

