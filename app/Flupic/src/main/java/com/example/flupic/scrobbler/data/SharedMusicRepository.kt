package com.example.flupic.scrobbler.data

interface SharedMusicRepository {

    suspend fun sendSharedMusicUpdate(parameters: SharedMusicUpdateParameters)

    suspend fun setSharedMusicActivityState(state: Boolean)
}