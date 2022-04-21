package com.anwera97.domain.mappers

import com.anwera97.data.composedclasses.ExpenditureWithDebtorsAndPayer
import com.anwera97.domain.models.ExpenditureModel

object ExpenditureMapper {

    fun toModel(entity: com.anwera97.data.composedclasses.ExpenditureWithDebtorsAndPayer): ExpenditureModel = with(entity) {
        return ExpenditureModel(
                uid = expenditure.id.toString(),
                payer = CompanionMapper.toModel(payer),
                debtors = debtors.map(CompanionMapper::toModel),
                detail = expenditure.detail,
                amountSpent = expenditure.expense,
                date = expenditure.date
        )
    }
}