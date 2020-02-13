package com.example.flupic.scrobbler.data

import android.location.Location
import com.example.flupic.scrobbler.model.ControllerMusicMetadata

interface SharedMusicRepository {

    suspend fun sendSharedMusicUpdate(metadata: ControllerMusicMetadata, location: Location)

    suspend fun sendSharedMusicUpdate(map: SharedMusicUpdateMap)

    suspend fun setSharedMusicActivityState(state: Boolean)
}