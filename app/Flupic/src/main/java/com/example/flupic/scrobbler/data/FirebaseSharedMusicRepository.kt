package com.example.flupic.scrobbler.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseSharedMusicRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SharedMusicRepository {

    private var latestPostedMusicUpdate: SharedMusicUpdateParameters? = null

    override suspend fun setSharedMusicActivityState(state: Boolean) { try { withContext(ioDispatcher) {
        val currentU = auth.currentUser
        val phoneNumber = currentU?.phoneNumber
        if (phoneNumber.isNullOrEmpty()) throw IllegalStateException("Can create user only after number verification")

        val ref = firestore.collection(SHARED_MUSIC_COLLECTION).document(currentU.uid)

        ref.update(mapOf(IS_ACTIVE to state)).await()

    } }catch (e: Exception) {
        Log.e(TAG, "sendSharedMusicMetadataUpdate() + ${e.message.toString()}")
    }}

    override suspend fun sendSharedMusicUpdate(parameters: SharedMusicUpdateParameters) {

        if(latestPostedMusicUpdate != null && !latestPostedMusicUpdate!!.updatableFrom(parameters)) return
        else
            latestPostedMusicUpdate = parameters

        try { withContext(ioDispatcher) {
            val currentU = auth.currentUser
            val phoneNumber = currentU?.phoneNumber
            if (phoneNumber.isNullOrEmpty()) throw IllegalStateException("Can create user only after number verification")

            val ref = firestore.collection(SHARED_MUSIC_COLLECTION).document(currentU.uid)

            ref.set(parameters.updateMap, SetOptions.merge()).await()
        } }catch (e: Exception) {
            Log.e(TAG, "sendSharedMusicUpdate() + ${e.message.toString()}")
        }
    }

    companion object{
        private const val TAG = "TAG SharedMusicRep"

        private const val SHARED_MUSIC_COLLECTION = "sharedMusic"
        private const val IS_ACTIVE = "isActive"
    }
}