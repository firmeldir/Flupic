package com.example.flupic.scrobbler.domain

import android.util.Log
import com.example.flupic.domain.UseCase
import com.example.flupic.scrobbler.data.SharingPointRepository
import com.example.flupic.scrobbler.model.PointLocation
import com.example.flupic.scrobbler.model.PointState
import javax.inject.Inject

class SendSharingPointUpdateUseCase @Inject constructor(
    private val sharingPointRepository: SharingPointRepository
): UseCase<SendSharingPointUpdateParameters, Unit>(){

    override suspend fun execute(parameters: SendSharingPointUpdateParameters) {
        val (state, location) = parameters

        Log.i(TAG, "SendSharingPointUpdate : ${state.position} : ${location.hash}")

        sharingPointRepository.sendSharingPointUpdate(state, location)
    }

    companion object{
        private const val TAG = "TAG SendSharingPUpd"
    }
}

data class SendSharingPointUpdateParameters(
    val pointState: PointState,
    val pointLocation: PointLocation
)
