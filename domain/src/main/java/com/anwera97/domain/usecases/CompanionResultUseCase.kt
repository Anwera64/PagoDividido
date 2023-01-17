package com.anwera97.domain.usecases

import com.anwera97.data.composedclasses.PayerWithExpendituresAndDebtors
import com.anwera97.data.repository.CompanionRepository
import com.anwera97.domain.mappers.CompanionMapper
import com.anwera97.domain.models.ResultModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CompanionResultUseCase @Inject constructor(private val companionRepository: CompanionRepository) {

    fun getPayersWithDebtors(tripId: Int): Flow<List<ResultModel>> {
        return companionRepository.getPayersWithDebtors(tripId).map(::transformResultEntities)
    }

    private fun transformResultEntities(list: List<PayerWithExpendituresAndDebtors>): List<ResultModel> {
        return list.map(CompanionMapper::toModel).let(CompanionMapper::makeCalculation)
    }
}