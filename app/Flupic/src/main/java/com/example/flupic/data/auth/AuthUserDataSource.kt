package com.example.flupic.data.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import javax.inject.Inject
import com.example.flupic.result.Result

/**
 * Some application auth logic : tokens etc
 */

//TODO Make FCM TOKENS Updater

interface AuthUserDataSource {

    fun getObservableBasicUserInfo(): LiveData<Result<BasicUserInfo>>

    fun getBasicUserInfo(): BasicUserInfo

    fun startListening()
    fun clearListener()
}

class FirebaseAuthUserDataSource @Inject constructor(
    val auth: FirebaseAuth
) : AuthUserDataSource {

    private var lastUid: String? = null

    private var isAlreadyListening = false

    private val currentUserObservable =
        MutableLiveData<Result<BasicUserInfo>>()


    private val authStateListener: ((FirebaseAuth) -> Unit) = { auth ->

        currentUserObservable.value  = Result.Success( FirebaseUserInfo(auth.currentUser) )
        lastUid = auth.uid
    }

    override fun getObservableBasicUserInfo(): LiveData<Result<BasicUserInfo>> = currentUserObservable

    override fun getBasicUserInfo(): BasicUserInfo = FirebaseUserInfo(auth.currentUser)



    override fun startListening() {
        if (!isAlreadyListening) {
            auth.addAuthStateListener(authStateListener)
            isAlreadyListening = true
        }
    }

    override fun clearListener() {
        auth.removeAuthStateListener(authStateListener)
        isAlreadyListening = false
    }
}
