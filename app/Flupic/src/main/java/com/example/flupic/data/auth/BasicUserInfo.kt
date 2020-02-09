package com.example.flupic.data.auth

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

interface BasicUserInfo{

    fun isSignedIn(): Boolean

    fun getUid(): String?


    fun getPhoneNumber(): String?

    fun getDisplayName(): String?

    fun getPhotoUrl(): Uri?


    fun getEmail(): String?

    fun isEmailVerified(): Boolean?


    fun getLastSignInTimestamp(): Long?

    fun getCreationTimestamp(): Long?
}

class FirebaseUserInfo(
    private val firebaseUser: FirebaseUser?
) : BasicUserInfo {

    override fun isSignedIn(): Boolean = firebaseUser != null

    override fun getUid(): String? = firebaseUser?.uid


    override fun getPhoneNumber(): String? = firebaseUser?.phoneNumber

    override fun getDisplayName(): String? = firebaseUser?.displayName

    override fun getPhotoUrl(): Uri? = firebaseUser?.photoUrl


    override fun getEmail(): String? = firebaseUser?.email

    override fun isEmailVerified(): Boolean? = firebaseUser?.isEmailVerified


    override fun getLastSignInTimestamp(): Long? = firebaseUser?.metadata?.lastSignInTimestamp

    override fun getCreationTimestamp(): Long? = firebaseUser?.metadata?.creationTimestamp
}