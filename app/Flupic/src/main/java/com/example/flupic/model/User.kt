package com.example.flupic.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val id: String,
    val username: String,
    val name: String,
    val bio: String,

    val photoUrl: String,
    val phoneNumber: String
)

data class FireUser(
    val username: String = "",
    val name: String = "",
    val bio: String = "",

    val photoUrl: String = "",
    val phoneNumber: String = "",
    @DocumentId val id: String = ""
)

