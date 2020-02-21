package com.example.flupic.scrobbler.data

import android.util.Log
import com.example.flupic.scrobbler.model.PointLocation
import com.example.flupic.scrobbler.model.PointMedia
import com.example.flupic.scrobbler.model.PointState
import com.example.flupic.scrobbler.model.SharingPoint
import com.example.flupic.scrobbler.model.firebase.FirePointLocation
import com.example.flupic.scrobbler.model.firebase.FirePointMedia
import com.example.flupic.scrobbler.model.firebase.FirePointState
import com.example.flupic.scrobbler.model.firebase.FireSharingPoint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseSharingPointRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SharingPointRepository {

    private var latestPostedSharingPointMedia: FirePointMedia? = null

    override suspend fun sendSharingPointUpdate(
        pointState: PointState,
        pointLocation: PointLocation
    ) {
        setPointUpdateMap(hashMapOf(
            STATE to FirePointState.from(pointState),
            LOCATION to FirePointLocation.from(pointLocation)
        ))
    }

    override suspend fun setSharingPointMedia(pointMedia: PointMedia) {
        val media = FirePointMedia.from(pointMedia)

        if(latestPostedSharingPointMedia?.updatableFor(media) == false)return

        latestPostedSharingPointMedia = media

        setPointUpdateMap(hashMapOf(
            MEDIA to media
        ))
    }

    override suspend fun setSharingPoint(sharingPoint: SharingPoint) { try { withContext(ioDispatcher) {
        val currentU = auth.currentUser
        val phoneNumber = currentU?.phoneNumber
        if (phoneNumber.isNullOrEmpty()) throw IllegalStateException("Not verified")

        val ref = firestore.collection(SHARED_MUSIC_COLLECTION).document(currentU.uid)

        ref.set(FireSharingPoint.from(sharingPoint), SetOptions.merge()).await()

    } }catch (e: Exception) {
        Log.e(TAG, "activateSharingPoint() + ${e.message.toString()}")
    }}

    override suspend fun setSharingPointActivationState(state: PointState) { try { withContext(ioDispatcher) {
        val currentU = auth.currentUser
        val phoneNumber = currentU?.phoneNumber
        if (phoneNumber.isNullOrEmpty()) throw IllegalStateException("Not verified")

        val ref = firestore.collection(SHARED_MUSIC_COLLECTION).document(currentU.uid)

        ref.update(mapOf(STATE to state)).await()

    } }catch (e: Exception) {
        Log.e(TAG, "setSharingPointActivationState() + ${e.message.toString()}")
    }}

    private suspend fun setPointUpdateMap(updateMap: Map<String, Any>) { try { withContext(ioDispatcher) {
        val currentU = auth.currentUser
        val phoneNumber = currentU?.phoneNumber
        if (phoneNumber.isNullOrEmpty()) throw IllegalStateException("Not verified")

        val ref = firestore.collection(SHARED_MUSIC_COLLECTION).document(currentU.uid)

        ref.set(updateMap, SetOptions.merge()).await()
    } }catch (e: Exception) {
        Log.e(TAG, "setPointUpdateMap() + ${e.message.toString()}")
    }
    }


    companion object{
        private const val TAG = "TAG SharingPointRep"

        private const val SHARED_MUSIC_COLLECTION = "sharingPoints"
        private const val STATE = "state"
        private const val LOCATION = "location"
        private const val MEDIA = "media"
    }
}