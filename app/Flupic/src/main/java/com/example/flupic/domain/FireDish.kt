package com.example.flupic.domain

import com.google.firebase.Timestamp
import java.util.*

data class FireDish(
    val name: String = "",
    var author: String = "",
    val description: String = "",
    var photo_url: String = "",
    val date: Timestamp = Timestamp(Date()),
    val likes: Int = 0
)