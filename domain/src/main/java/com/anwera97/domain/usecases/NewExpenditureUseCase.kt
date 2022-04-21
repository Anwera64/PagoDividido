package com.anwera97.domain.usecases

import com.anwera97.data.entities.Companion
import com.anwera97.data.entities.Expenditure
import com.anwera97.data.repository.CompanionRepository
import com.anwera97.data.repository.ExpenditureRepository
import com.anwera97.domain.mappers.CompanionMapper
import com.anwera97.domain.models.CompanionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class NewExpenditureUseCase(
    private val companionRepository: CompanionRepository,
    private val expenditureRepository: ExpenditureRepository
) {
    suspend fun addExpenditure(
        tripId: Int,
        payerId: Int,
        debtors: HashMap<Int, Double>,
        detail: String?,
        amountSpent: Double
    ) {
        val expenditure = Expenditure(
            expense = amountSpent,
            imageRef = null,
            date = Date(),
            tripId = tripId,
            payerId = payerId,
            detail = if (detail.isNullOrEmpty()) null else detail
        )
        expenditureRepository.addExpenditure(expenditure = expenditure, debtorIds = debtors)
    }

    fun getTripCompanions(tripId: Int): Flow<List<CompanionModel>> {
        return companionRepository.getTripCompanions(tripId).map(this::mapToCompanionList)
    }

    private fun mapToCompanionList(list: List<Companion>): List<CompanionModel> {
        return list.map(CompanionMapper::toModel)
    }
}