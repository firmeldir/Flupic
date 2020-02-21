package com.example.flupic.scrobbler.data

import com.example.flupic.scrobbler.model.PointLocation
import com.example.flupic.scrobbler.model.PointMedia
import com.example.flupic.scrobbler.model.PointState
import com.example.flupic.scrobbler.model.SharingPoint

interface SharingPointRepository {

    suspend fun sendSharingPointUpdate(pointState: PointState, pointLocation: PointLocation)

    suspend fun setSharingPointMedia(pointMedia: PointMedia)

    suspend fun setSharingPoint(sharingPoint: SharingPoint)

    suspend fun setSharingPointActivationState(state: PointState)
}

//    suspend fun sendSharingPointUpdate(pointState: PointState, pointLocation: PointLocation)
//
//    suspend fun updateSharingPointMedia(pointMedia: PointMedia)
//
//    suspend fun activateSharingPoint(sharingPoint: SharingPoint)
//
//    suspend fun setSharingPointActivationState(state: Boolean)