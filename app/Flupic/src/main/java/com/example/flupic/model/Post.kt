package com.example.flupic.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Post (
    @DocumentId val id: String,
    val title: String,
    val photoUrl: String,
    val date: Timestamp
)

data class FirePost (
    val title: String = "",
    val photoUrl: String = "",
    val date: Timestamp? = null,
    @DocumentId val id: String = ""
)