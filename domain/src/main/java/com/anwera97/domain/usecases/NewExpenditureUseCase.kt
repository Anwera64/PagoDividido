package com.anwera97.domain.usecases

import com.anwera97.data.entities.Expenditure
import com.anwera97.data.repository.ExpenditureRepository
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.DebtorInputError
import com.anwera97.domain.models.DebtorInputErrorReasons
import com.anwera97.domain.models.InputErrorType
import java.util.*
import javax.inject.Inject

class NewExpenditureUseCase @Inject constructor(
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

    fun checkAmountInputError(amountString: String): InputErrorType {
        return when {
            amountString.isEmpty() -> InputErrorType.INPUT_AMOUNT
            //we check if we can cast it to double first to avoid errors
            amountString.toDoubleOrNull() == null -> InputErrorType.INPUT_NUMBER
            else -> InputErrorType.NO_ERROR
        }
    }

    fun checkPaymentOptionError(exists: Boolean): InputErrorType {
        return when {
            !exists -> InputErrorType.MISSING_FIELD
            else -> InputErrorType.NO_ERROR
        }
    }

    fun checkPayerInputError(id: Int?): InputErrorType {
        return when (id) {
            -1 -> InputErrorType.INEXISTENT_ID
            null -> InputErrorType.MISSING_FIELD
            else -> InputErrorType.NO_ERROR
        }
    }

    fun createEqualPayments(
        companions: List<CompanionModel>,
        amountSpent: Double
    ): MutableMap<Int, Double> {
        val equalizedMap = mutableMapOf<Int, Double>()
        companions.forEach { companionModel ->
            equalizedMap[companionModel.uid.toInt()] = amountSpent / companions.size
        }
        return equalizedMap
    }

    fun completeDebtMap(
        paymentRelationship: Map<Int, Double>,
        companions: List<CompanionModel>,
    ): MutableMap<Int, Double> {
        val completeMap = paymentRelationship.toMutableMap()
        for (companion in companions) {
            val companionId = companion.uid.toInt()
            val entryExists = completeMap[companionId] != null
            if (entryExists) continue
            completeMap[companionId] = 0.0
        }
        return completeMap
    }
}