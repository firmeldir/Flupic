package com.example.flupic.domain

import com.google.firebase.Timestamp
import java.util.*

data class FireDish(
    val name: String = "",
    var author: String = "",
    val description: String = "",
    var photo_url: String = "",
    val date: Timestamp = Timestamp(Date()),
    val likes: List<String> = listOf()
)

data class FlupicDish(
    val dish: FireDish,
    val dishId: String,
    val authorId: String
) : Comparable<FlupicDish>{

    override fun compareTo(other: FlupicDish): Int = this.dish.date.compareTo(other.dish.date)

}