package com.example.flupic.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flupic.TAG
import com.example.flupic.domain.FireUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class ProfileViewModel : ViewModel(), EventListener<DocumentSnapshot>{

    //Firebase
    private val auth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("users").document(auth.uid.toString())

    //User
    private val _userData: MutableLiveData<FireUser> = MutableLiveData()
    val userData: LiveData<FireUser>
        get() = _userData


    //Listener
    private val listener : ListenerRegistration? = ref.addSnapshotListener(this)

    override fun onEvent(p0: DocumentSnapshot?, p1: FirebaseFirestoreException?) {
        p1?.let {
            Log.e(TAG, "Error in ProfileViewModel : ${p1.message}")
            return
        }

        p0?.let {
            val user = p0.toObject(FireUser::class.java)
            _userData.value = user
        }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.let {
            listener.remove()
        }
    }
}