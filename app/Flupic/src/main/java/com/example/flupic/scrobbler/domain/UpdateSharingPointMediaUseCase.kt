package com.example.flupic.scrobbler.domain

import com.example.flupic.domain.UseCase
import com.example.flupic.scrobbler.data.SharingPointRepository
import com.example.flupic.scrobbler.model.PointMedia
import javax.inject.Inject

class UpdateSharingPointMediaUseCase @Inject constructor(
    private val sharingPointRepository: SharingPointRepository
): UseCase<PointMedia, Unit>(){

    override suspend fun execute(parameters: PointMedia) {
        sharingPointRepository.setSharingPointMedia(parameters)
    }
}