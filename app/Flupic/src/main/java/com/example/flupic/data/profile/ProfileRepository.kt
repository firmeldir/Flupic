package com.example.flupic.data.profile

interface ProfileRepository{

    suspend fun getUsersProfile(uid: String, forceUpdate: Boolean)

    suspend fun refreshUsersProfile(uid: String)
}