package com.example.flupic.model

data class User(
    val id: String,
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
    val phoneNumber: String = ""
)

