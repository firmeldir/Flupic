package com.example.flupic.scrobbler.domain

import com.example.flupic.domain.UseCase
import com.example.flupic.scrobbler.data.SharingPointRepository
import com.example.flupic.scrobbler.model.SharingPoint
import javax.inject.Inject

class ActivateSharingPointUseCase @Inject constructor(
    private val sharingPointRepository: SharingPointRepository
): UseCase<SharingPoint, Unit>(){

    override suspend fun execute(parameters: SharingPoint) {
        sharingPointRepository.setSharingPoint(parameters)
    }
}