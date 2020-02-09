package com.example.flupic.util.handler

import android.app.Application
import android.util.Log
import com.example.flupic.model.AuthenticationUser
import com.example.flupic.ui.base.OperableViewModel
import com.example.flupic.util.exception.AuthException
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.CredentialsClient
import javax.inject.Inject

class SmartLockHandler  @Inject constructor(
    application: Application,
    private val credentialsClient: CredentialsClient
): OperableViewModel<AuthenticationUser>(application){

    fun saveCredentials(credential: Credential?, authUser: AuthenticationUser){
        onOperationLoading()

        if (credential == null) { setOperationResult(
            AuthException(
                "Failed to build credential."
            )
        ); return }

        credentialsClient.save(credential)
            .addOnSuccessListener {
                setOperationResult(authUser)
            }
            .addOnFailureListener {e ->
                Log.e(TAG, e.message.toString())
                setOperationResult(e)
            }
    }

    companion object{
        private const val TAG = "TAG SmartLockHandler"
    }
}