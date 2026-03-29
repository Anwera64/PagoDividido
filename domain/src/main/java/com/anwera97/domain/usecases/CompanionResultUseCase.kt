package com.anwera97.domain.usecases

import com.anwera97.domain.models.ResultModel
import com.anwera97.domain.repositories.CompanionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CompanionResultUseCase @Inject constructor(private val companionRepository: CompanionRepository) {

    fun getPayersWithDebtors(tripId: Int, companionId: Int): Flow<ResultModel?> {
        return companionRepository
            .getResultInfoFor(tripId)
            .map { resultModels ->
                calculateEndResultForCompanion(resultModels, companionId.toString())
            }
    }

    /**
     * Returns the amount owed to and from for a single Companion.
     */
    private fun calculateEndResultForCompanion(
        results: List<ResultModel>,
        requestedId: String
    ): ResultModel? {
        // Look for the result. If there is none, return null
        val requestedResult: ResultModel = results.find { resultModel ->
            resultModel.companion.uid == requestedId
        } ?: return null
        val requestedCompanion = requestedResult.companion

        // Remove the requested result to avoid nulling their own debt
        val companionResults = results.toMutableList()
        companionResults.remove(requestedResult)

        companionResults.forEach { companionResult ->
            val otherCompanion = companionResult.companion
            val currentDebt: Double = requestedResult.debts[otherCompanion] ?: 0.0
            val amountOwed: Double = companionResult.debts[requestedCompanion] ?: 0.0
            requestedResult.debts[otherCompanion] = currentDebt - amountOwed
        }
        return requestedResult
    }
}
