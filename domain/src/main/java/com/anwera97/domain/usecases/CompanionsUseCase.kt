package com.anwera97.domain.usecases

import com.anwera97.data.entities.Companion
import com.anwera97.data.repository.CompanionRepository
import com.anwera97.domain.mappers.CompanionMapper
import com.anwera97.domain.models.CompanionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CompanionsUseCase @Inject constructor(
    private val companionRepository: CompanionRepository,
) {

    fun getTripCompanions(tripId: Int): Flow<List<CompanionModel>> {
        return companionRepository.getTripCompanions(tripId).map(this::mapToCompanionList)
    }

    private fun mapToCompanionList(list: List<Companion>): List<CompanionModel> {
        return list.map(CompanionMapper::toModel)
    }
}