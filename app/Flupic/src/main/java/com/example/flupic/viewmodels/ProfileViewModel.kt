package com.example.flupic.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flupic.TAG
import com.example.flupic.domain.FireUser
import com.example.flupic.repository.InsideRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(val insideRepository: InsideRepository, val auth : FirebaseAuth, db : FirebaseFirestore) : ViewModel(), EventListener<DocumentSnapshot>{

    //Coroutines
    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    //Profile data
    private val _isSubscribed: MutableLiveData<Boolean> = MutableLiveData()
    val isSubscribed: LiveData<Boolean>
        get() = _isSubscribed

    private fun getIsSubscribed(){
        coroutineScope.launch {
            insideRepository.isSubscribed(profileUID)?.let { _isSubscribed.value = it }
        }
    }

    //User
    private val _userData: MutableLiveData<FireUser> = MutableLiveData()
    val userData: LiveData<FireUser>
        get() = _userData

    private val usersRef = db.collection("users")
    val userUID = auth.uid
    lateinit var profileUID: String


    //Listener
    private var listener : ListenerRegistration? = null

    //Listen User
    fun listenUser(uid: String = userUID.toString()){
        val userRef = usersRef.document(uid)
        profileUID = uid
        listener = userRef.addSnapshotListener(this@ProfileViewModel)
        getIsSubscribed()
    }

    override fun onEvent(p0: DocumentSnapshot?, p1: FirebaseFirestoreException?) {
        p1?.let {
            Log.e(TAG, "onEvent() : ${p1.message}")
            return
        }

        p0?.let {
            val user = p0.toObject(FireUser::class.java)
            _userData.value = user
        }
    }

    fun subscribe(){
        coroutineScope.launch {
            isSubscribed.value?.let {
                insideRepository.subscribe(profileUID,isSubscribed.value!!)?.let {
                    _isSubscribed.value = it
                }
            }
        }
    }

    fun like(accessId: String, authorId: String){
        coroutineScope.launch {
            insideRepository.like(accessId, authorId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()

        coroutineJob.cancel()
    }
}