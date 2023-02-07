package com.anwera97.domain.mappers

import com.anwera97.data.composedclasses.DebtWithCompanion
import com.anwera97.data.composedclasses.ExpenditureWithDebtorsAndPayer
import com.anwera97.data.composedclasses.ExpenditureWithPayer
import com.anwera97.domain.models.DebtorModel
import com.anwera97.domain.models.ExpenditureDetailModel
import com.anwera97.domain.models.ExpenditureModel

object ExpenditureMapper {

    fun toModel(entity: ExpenditureWithPayer): ExpenditureModel = with(entity) {
        return ExpenditureModel(
            uid = expenditure.id.toString(),
            payer = CompanionMapper.toModel(payer),
            detail = expenditure.detail,
            amountSpent = expenditure.expense,
            date = expenditure.date
        )
    }

    fun toModel(entity: ExpenditureWithDebtorsAndPayer): ExpenditureDetailModel = with(entity) {
        return ExpenditureDetailModel(
            uid = expenditure.id.toString(),
            payer = CompanionMapper.toModel(payer),
            detail = expenditure.detail,
            amountSpent = expenditure.expense,
            date = expenditure.date,
            debtors = debtors.map(::toModel)
        )
    }

    private fun toModel(debtWithCompanion: DebtWithCompanion): DebtorModel {
        return DebtorModel(
            id = debtWithCompanion.companion.id,
            amount = debtWithCompanion.debtors.amount,
            name = debtWithCompanion.companion.name
        )
    }
}