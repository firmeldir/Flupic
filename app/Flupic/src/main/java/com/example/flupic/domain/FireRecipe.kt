package com.example.flupic.domain


data class FireRecipe(
    val cookTimeMin: Int = 0,
    val peoplePerServing: Int = 0,
    var ingredients: List<String> = listOf(),
    var recipe: Map<String, String> = mapOf()
)