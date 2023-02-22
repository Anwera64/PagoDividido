package com.anwera97.domain.usecases

import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.domain.repositories.ExpenditureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenditureUseCase @Inject constructor(private val expenditureRepository: ExpenditureRepository) {

    fun getExpenditures(tripUid: Int): Flow<List<ExpenditureModel>> {
        return expenditureRepository.getExpense(tripUid)
    }
}