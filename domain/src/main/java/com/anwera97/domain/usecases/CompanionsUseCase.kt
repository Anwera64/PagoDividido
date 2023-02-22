package com.anwera97.domain.usecases

import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.repositories.CompanionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompanionsUseCase @Inject constructor(
    private val companionRepository: CompanionRepository,
) {

    fun getTripCompanions(tripId: Int): Flow<List<CompanionModel>> {
        return companionRepository.getTripCompanions(tripId)
    }
}