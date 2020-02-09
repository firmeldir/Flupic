package com.example.flupic.data.users

import android.net.Uri
import com.example.flupic.model.User

interface UserManagementDataSource{

    /*
    *   Fetch user data
    */

    suspend fun getUser(): User



    /*
    *   Edit user data
    */

    suspend fun createFullRegUser(username: String, photoUri: Uri? = null)

    suspend fun setUsername(username: String)

    suspend fun setUserPhoto(uri: Uri)

    suspend fun setExtendedUserInfo(
        username: String? = null,
        name: String? = null,
        bio: String? = null)
}