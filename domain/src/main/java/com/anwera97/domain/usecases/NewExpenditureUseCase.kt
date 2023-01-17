package com.anwera97.domain.usecases

import com.anwera97.data.entities.Companion
import com.anwera97.data.entities.Expenditure
import com.anwera97.data.repository.CompanionRepository
import com.anwera97.data.repository.ExpenditureRepository
import com.anwera97.domain.mappers.CompanionMapper
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.DebtorInputError
import com.anwera97.domain.models.DebtorInputErrorReasons
import com.anwera97.domain.models.InputErrorTypes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class NewExpenditureUseCase @Inject constructor(
    private val companionRepository: CompanionRepository,
    private val expenditureRepository: ExpenditureRepository
) {
    suspend fun addExpenditure(
        tripId: Int,
        payerId: Int,
        debtors: Map<Int, Double>,
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

    fun lookForDebtorInputErrors(
        totalAmount: Double,
        debtors: Map<Int, Double>
    ): List<DebtorInputError> {
        val reasons: MutableList<DebtorInputError> = ArrayList()
        var accumulatedDebt = 0.0
        debtors.forEach { entry ->
            val reason = checkDebtorInput(entry.value, accumulatedDebt, totalAmount)
            addDebtorInputError(reason, entry, reasons)
            accumulatedDebt = entry.value
        }
        return reasons
    }

    fun checkAmountsDifference(totalAmount: Double, debtors: Map<Int, Double>): Double {
        var accumulatedDebt = 0.0
        debtors.forEach { entry ->
            accumulatedDebt += entry.value
        }
        return totalAmount - accumulatedDebt
    }

    private fun addDebtorInputError(
        reason: DebtorInputErrorReasons?,
        entry: Map.Entry<Int, Double>,
        reasons: MutableList<DebtorInputError>
    ) {
        if (reason == null) return
        val debtorInputError = DebtorInputError(entry.key, reason)
        reasons.add(debtorInputError)
    }

    private fun checkDebtorInput(
        amount: Double,
        accumulatedDebt: Double,
        maxAmount: Double
    ): DebtorInputErrorReasons? {
        return when {
            amount <= 0.0 -> DebtorInputErrorReasons.ZERO_OR_NEGATIVE
            amount > maxAmount -> DebtorInputErrorReasons.OVER_MAX_AMOUNT
            accumulatedDebt + amount > maxAmount -> DebtorInputErrorReasons.SUM_EXCEEDS_MAX_AMOUNT
            else -> null
        }
    }

    fun getTripCompanions(tripId: Int): Flow<List<CompanionModel>> {
        return companionRepository.getTripCompanions(tripId).map(this::mapToCompanionList)
    }

    private fun mapToCompanionList(list: List<Companion>): List<CompanionModel> {
        return list.map(CompanionMapper::toModel)
    }

    fun checkAmountInputError(amountString: String): InputErrorTypes {
        return when {
            amountString.isEmpty() -> InputErrorTypes.INPUT_AMOUNT
            //we check if we can cast it to double first to avoid errors
            amountString.toDoubleOrNull() == null -> InputErrorTypes.INPUT_NUMBER
            else -> InputErrorTypes.NO_ERROR
        }
    }

    fun checkPayerInputError(id: Int): InputErrorTypes {
        return if (id == -1) {
            InputErrorTypes.INEXISTENT_ID
        } else {
            InputErrorTypes.NO_ERROR
        }
    }
}