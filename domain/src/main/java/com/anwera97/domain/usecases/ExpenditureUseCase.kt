package com.anwera97.domain.usecases

import com.anwera97.data.composedclasses.ExpenditureWithPayer
import com.anwera97.data.repository.ExpenditureRepository
import com.anwera97.domain.mappers.ExpenditureMapper
import com.anwera97.domain.models.ExpenditureDetailModel
import com.anwera97.domain.models.ExpenditureModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenditureUseCase @Inject constructor(private val expenditureRepository: ExpenditureRepository) {

    fun getExpediture(id: Int): Flow<ExpenditureDetailModel> {
        return expenditureRepository.getExpenditure(id).map(ExpenditureMapper::toModel)
    }

    fun getExpenditures(tripUid: Int): Flow<List<ExpenditureModel>> {
        return expenditureRepository.getExpenditures(tripUid).map(::mapToExpenditureModels)
    }

    private fun mapToExpenditureModels(list: List<ExpenditureWithPayer>): List<ExpenditureModel> {
        return list.map(ExpenditureMapper::toModel)
    }
}