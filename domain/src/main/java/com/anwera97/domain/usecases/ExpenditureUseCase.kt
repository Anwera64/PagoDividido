package com.anwera97.domain.usecases

import com.anwera97.data.composedclasses.ExpenditureWithDebtorsAndPayer
import com.anwera97.data.repository.ExpenditureRepository
import com.anwera97.domain.mappers.ExpenditureMapper
import com.anwera97.domain.models.ExpenditureModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenditureUseCase(private val expenditureRepository: ExpenditureRepository) {

    fun getExpenditures(tripUid: Int): Flow<List<ExpenditureModel>> {
        return expenditureRepository.getExpenditures(tripUid).map(::mapToExpenditureModels)
    }

    private fun mapToExpenditureModels(list: List<ExpenditureWithDebtorsAndPayer>): List<ExpenditureModel> {
        return list.map(ExpenditureMapper::toModel)
    }
}