package com.example.flupic.model

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.PhoneAuthProvider

// * * *SINCE ONLY PHONE AUTH* * *//

data class AuthenticationUser(
    var isNewUser: Boolean = false,

    val pendingCredential: AuthCredential,

    val accountType: String = PHONE_IDENTITY,
    val providerId: String = PhoneAuthProvider.PROVIDER_ID,

    val password: String? = null,
    var exception: Exception? = null
){

    companion object{
        private const val PHONE_IDENTITY = "https://phone.firebase"
    }
}