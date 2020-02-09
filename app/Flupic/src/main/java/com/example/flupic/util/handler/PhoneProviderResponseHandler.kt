package com.example.flupic.util.handler

import android.app.Application
import android.util.Log
import com.example.flupic.di.subcomponent.AuthScope
import com.example.flupic.model.AuthenticationUser
import com.example.flupic.ui.base.OperableViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import javax.inject.Inject

@AuthScope
class PhoneProviderResponseHandler @Inject constructor(
    application: Application,
    val auth: FirebaseAuth
): OperableViewModel<AuthenticationUser>(application){

    fun startSignIn(credential: PhoneAuthCredential, authUser: AuthenticationUser){
        onOperationLoading()

        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                authUser.isNewUser = it.additionalUserInfo?.isNewUser ?: false
                setOperationResult(authUser)
            }
            .addOnFailureListener {e ->
                Log.e(TAG, e.message.toString())
                setOperationResult(e)
            }
    }

    fun getCurrentUser() = auth.currentUser

    companion object{
        private const val TAG = "TAG PhoneProvRespHand"

        const val VERIFICATION_CODE_LENGTH = 6
    }
}