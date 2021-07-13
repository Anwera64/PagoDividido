package com.anwera64.pagodividido.domain.mappers

import com.anwera64.pagodividido.data.composedclasses.ExpenditureWithDebtors
import com.anwera64.pagodividido.domain.models.ExpenditureModel

object ExpenditureMapper {

    fun toModel(entity: ExpenditureWithDebtors): ExpenditureModel = with(entity) {
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