package com.example.flupic.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.auth.User
import java.util.*

data class FireUserLocation(
    val geo_point: GeoPoint? = null,
    val timestamp: Timestamp = Timestamp(Date())
)

data class UserInfoLocation(
    var uid: String = "",
    val fireUserLocation: FireUserLocation? = null,
    var username: String = "",
    var photo_url: String = "",
    var publications: Int = 0
){

    fun putUserInfo(user: FireUser, uid: String){
        this.uid = uid
        username = user.username
        photo_url = user.photo_url
        publications = user.publications
    }

    companion object{
        fun getLocInstance(fireUserLocation: FireUserLocation): UserInfoLocation = UserInfoLocation(fireUserLocation = fireUserLocation)
    }
}