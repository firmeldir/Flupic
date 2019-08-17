package com.example.flupic.domain

data class FireUser (
    val username: String = "",
    val fullname: String = "",
    val bio: String = "",
    val category: String = "",
    val phone_num: String = "",
    val photo_url: String = "",
    val followers: Int = 0,
    val publications: Int = 0,
    val following: List<String> = listOf()
)