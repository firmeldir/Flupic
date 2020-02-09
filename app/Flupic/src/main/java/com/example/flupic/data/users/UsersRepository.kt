package com.example.flupic.data.users

import com.example.flupic.model.User
import com.google.firebase.firestore.Query

interface UsersRepository {

    suspend fun getUser(forceUpdate: Boolean) : User

    suspend fun getPeopleByUid(uid: String, forceUpdate: Boolean) : User
}