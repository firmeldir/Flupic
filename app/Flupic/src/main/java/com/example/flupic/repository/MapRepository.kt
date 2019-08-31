package com.example.flupic.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.flupic.TAG
import com.example.flupic.domain.FireUser
import com.example.flupic.domain.FireUserLocation
import com.example.flupic.domain.UserInfoLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val auth : FirebaseAuth,
    private val db : FirebaseFirestore,
    private val storage : FirebaseStorage,
    private val locationProvider : FusedLocationProviderClient,
    private val context: Context

){

    suspend fun lastKnownLocation(): FireUserLocation?{

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_DENIED) {
            return null
        }

        try {
            val location = locationProvider.lastLocation.await()
            return FireUserLocation(GeoPoint(location.latitude, location.longitude))

        }catch (e: Exception){
            Log.e(TAG,"lastKnownLocation() : ${e.message}")
        }

        return null
    }

    suspend fun putBusinessLocation(userLocation: FireUserLocation): Boolean{
        val userLocationRef = db.collection("users_business_location").document(auth.uid.toString())

        return try {
            withContext(Dispatchers.IO){
                userLocationRef.set(userLocation).await()
            }
            true
        }catch (e: Exception){
            Log.e(TAG,"putBusinessLocation() : ${e.message}")
            false
        }
    }

    suspend fun businessLocation(): List<UserInfoLocation>?{

        val locationRef = db.collection("users_business_location")
        val infoLocations: MutableList<UserInfoLocation> = mutableListOf()

        try {
            withContext(Dispatchers.IO){
                coroutineScope {

                    val documents = locationRef.get().await().documents
                    for(document in documents){

                        val userInfoLocation = UserInfoLocation.getLocInstance(document.toObject(FireUserLocation::class.java)!!)

                        val id = document.reference.id
                        val user = db.collection("users").document(id).get().await().toObject(FireUser::class.java)!!
                        userInfoLocation.putUserInfo(user, id)

                        infoLocations.add(userInfoLocation)
                    }
                }
            }

            return infoLocations

        }catch (e: Exception){
            Log.e(TAG,"usersLocation() : ${e.message}")
        }

        return null
    }
}