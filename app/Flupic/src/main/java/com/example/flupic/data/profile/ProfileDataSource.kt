package com.example.flupic.data.profile

interface ProfileDataSource {

    suspend fun getUsersProfile(uid: String)

    suspend fun saveUsersProfile(uid: String)
}