package com.example.flupic.scrobbler.data

import android.location.Location
import android.util.Log
import com.example.flupic.data.users.FirebaseUserManagementDataSource
import com.example.flupic.scrobbler.model.ControllerMusicMetadata
import com.example.flupic.scrobbler.model.FireSharedMusic
import com.example.flupic.scrobbler.model.SharedMusic
import com.example.flupic.scrobbler.util.toGeoPoint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.imperiumlabs.geofirestore.GeoLocation
import org.imperiumlabs.geofirestore.core.GeoHash
import javax.inject.Inject

class FirebaseSharedMusicRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SharedMusicRepository {

    override suspend fun sendSharedMusicUpdate(
        metadata: ControllerMusicMetadata,
        location: Location
    )  { try { withContext(ioDispatcher) {
        val currentU = auth.currentUser
        val phoneNumber = currentU?.phoneNumber
        if (phoneNumber.isNullOrEmpty()) throw IllegalStateException("Can create user only after number verification")

        val ref = firestore.collection(SHARED_MUSIC_COLLECTION).document(currentU.uid)
        val geoHash = GeoHash(GeoLocation(location.latitude, location.longitude)).geoHashString

        val sharedMusic = FireSharedMusic.fromControllerMusicMetadata(metadata, location.toGeoPoint(), geoHash)

        ref.set(sharedMusic, SetOptions.merge()).await()
    } }catch (e: Exception) {
        Log.e(TAG, "sendSharedMusicUpdate() + ${e.message.toString()}")
    } }

    override suspend fun sendSharedMusicUpdate(map: SharedMusicUpdateMap) { try { withContext(ioDispatcher) {
        val currentU = auth.currentUser
        val phoneNumber = currentU?.phoneNumber
        if (phoneNumber.isNullOrEmpty()) throw IllegalStateException("Can create user only after number verification")

        val ref = firestore.collection(SHARED_MUSIC_COLLECTION).document(currentU.uid)

        ref.set(map.updateMap, SetOptions.merge()).await()
    } }catch (e: Exception) {
        Log.e(TAG, "sendSharedMusicMetadataUpdate() + ${e.message.toString()}")
    }}

    override suspend fun setSharedMusicActivityState(state: Boolean) { try { withContext(ioDispatcher) {
        val currentU = auth.currentUser
        val phoneNumber = currentU?.phoneNumber
        if (phoneNumber.isNullOrEmpty()) throw IllegalStateException("Can create user only after number verification")

        val ref = firestore.collection(SHARED_MUSIC_COLLECTION).document(currentU.uid)

        ref.update(mapOf(IS_ACTIVE to state)).await()

    } }catch (e: Exception) {
        Log.e(TAG, "sendSharedMusicMetadataUpdate() + ${e.message.toString()}")
    }}

    companion object{
        private const val TAG = "TAG SharedMusicRep"

        private const val SHARED_MUSIC_COLLECTION = "sharedMusic"
        private const val IS_ACTIVE = "isActive"

    }
}