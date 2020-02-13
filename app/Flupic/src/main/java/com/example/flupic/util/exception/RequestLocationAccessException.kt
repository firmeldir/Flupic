package com.example.flupic.util.exception

class RequestLocationAccessException(private val type: LocationAccessType) : Exception("Request location Access of $type")

enum class LocationAccessType{ ACCESS_COARSE_LOCATION, ACCESS_BACKGROUND_LOCATION }