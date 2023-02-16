package com.anwera97.data.mappers

import com.anwera97.data.composedclasses.ExpenditureWithDebtorsAndPayer
import com.anwera97.data.entities.Expenditure
import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.domain.models.ExpenseCreationData
import kotlin.math.exp

object ExpenseMapper {

    fun toModel(entity: ExpenditureWithDebtorsAndPayer): ExpenditureModel = with(entity) {
        return ExpenditureModel(
            uid = expenditure.id.toString(),
            payer = CompanionMapper.toModel(payer),
            debtors = debtors.map(CompanionMapper::toModel),
            detail = expenditure.detail,
            amountSpent = expenditure.expense,
            date = expenditure.date
        )
    }

    fun toEntity(expenseCreationData: ExpenseCreationData): Expenditure =
        with(expenseCreationData) {
            return Expenditure(
                id = id,
                expense = expense,
                imageRef = null,
                date = date,
                tripId = tripId,
                payerId = payerId,
                detail = detail
            )
        }
}