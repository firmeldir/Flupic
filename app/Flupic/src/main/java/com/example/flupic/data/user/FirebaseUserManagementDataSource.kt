package com.example.flupic.data.user

import android.net.Uri
import android.util.Log
import com.example.flupic.R
import com.example.flupic.model.FireUser
import com.example.flupic.model.User
import com.example.flupic.util.exception.ResourceableException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import javax.inject.Inject

class FirebaseUserManagementDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): UserManagementDataSource{

    override suspend fun getUser(): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun createFullRegUser(username: String, photoUri: Uri?) = withContext(ioDispatcher){
        if(username.isEmpty()) throw ResourceableException(R.string.not_valid_value)

        val currentU = auth.currentUser
        val phoneNumber = currentU?.phoneNumber
        if(phoneNumber.isNullOrEmpty()) throw IllegalStateException("Can create user only after number verification")

        val ref =  firestore.collection(USERS_COLLECTION).whereEqualTo(USERNAME, username)
        val conflictsU = ref.get().await()

        if(!conflictsU.isEmpty) throw ResourceableException(R.string.username_already_exist)

        val storageUri = photoUri?.let {
            return@let uploadUserPhotoToStorage(photoUri)
        }

        val newU = FireUser(username, "", "", storageUri?.toString() ?: "", phoneNumber)

        firestore.collection(USERS_COLLECTION).document(currentU.uid).set(newU).await()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .setPhotoUri(storageUri)
            .build()

        currentU.updateProfile(profileUpdates).await()

        Unit
    }

    override suspend fun setUsername(username: String) = withContext(ioDispatcher){
        if(username.isEmpty()) throw ResourceableException(R.string.not_valid_value)

        val currentU = auth.currentUser
        val phoneNumber = currentU?.phoneNumber
        if(phoneNumber.isNullOrEmpty()) throw IllegalStateException("Can create user only after number verification")

        val conflictRef =  firestore.collection(USERS_COLLECTION).whereEqualTo(USERNAME, username)
        val conflictsU = conflictRef.get().await()

        if(!conflictsU.isEmpty) throw ResourceableException(R.string.username_already_exist)

        val editURef = firestore.collection(USERS_COLLECTION).document(currentU.uid)

        editURef.update(mapOf(USERNAME to username)).await()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()

        currentU.updateProfile(profileUpdates).await()

        //

        conflictRef.get()

        Unit
    }

    override suspend fun setUserPhoto(uri: Uri) {

        val currentU = auth.currentUser
        val phoneNumber = currentU?.phoneNumber
        if(phoneNumber.isNullOrEmpty()) throw IllegalStateException("Can create user only after number verification")

        val storageUri = uploadUserPhotoToStorage(uri)

        val editURef = firestore.collection(USERS_COLLECTION).document(currentU.uid)

        editURef.update(mapOf(PHOTO_URL to storageUri)).await()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(storageUri)
            .build()

        currentU.updateProfile(profileUpdates).await()

        Unit
    }

    override suspend fun setExtendedUserInfo(username: String?, name: String?, bio: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private suspend fun uploadUserPhotoToStorage(uri: Uri) : Uri{

        val photoRef = storage.reference.child("$USERS_COLLECTION/${auth.uid}/$AVATAR_FILE")

        try { photoRef.delete().await() }catch(e: Exception){ }

        val task = photoRef.putFile(uri).continueWithTask{

            if (!it.isSuccessful) throw it.exception ?: ResourceableException(R.string.error_while_uploading_photo)

            return@continueWithTask photoRef.downloadUrl
        }

        return task.await()
    }

    companion object{
        private const val USERS_COLLECTION = "users"
        private const val USERNAME = "username"
        private const val PHOTO_URL = "photoUrl"
        private const val AVATAR_FILE = "avatar.jpg"
    }

}