package com.example.flupic.model

import com.google.firebase.auth.PhoneAuthCredential

data class PhoneVerification(
    val number: String,
    val credential: PhoneAuthCredential,
    val isAutoVerified: Boolean
)