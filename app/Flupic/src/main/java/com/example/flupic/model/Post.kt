package com.example.flupic.model

import com.google.firebase.Timestamp

data class Post (
    val id: String,
    val title: String,
    val photoUrl: String,
    val date: Timestamp
)

data class FirePost (
    val title: String = "",
    val photoUrl: String = "",
    val date: Timestamp? = null
)