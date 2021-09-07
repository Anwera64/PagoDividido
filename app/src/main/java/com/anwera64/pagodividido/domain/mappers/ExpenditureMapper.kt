package com.anwera64.pagodividido.domain.mappers

import com.anwera64.pagodividido.data.composedclasses.ExpenditureDetailEntity
import com.anwera64.pagodividido.data.composedclasses.ExpenditureWithDebtorsAndPayer
import com.anwera64.pagodividido.domain.models.ExpenditureDetail
import com.anwera64.pagodividido.domain.models.ExpenditureModel

object ExpenditureMapper {

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

    fun toDetail(entity: ExpenditureDetailEntity): ExpenditureDetail = with(entity) {
        return ExpenditureDetail(
            uid = expenditure.id.toString(),
            payer = CompanionMapper.toModel(payer),
            debtors = debtors.map(CompanionMapper::toDebtor),
            detail = expenditure.detail,
            amountSpent = expenditure.expense,
            date = expenditure.date
        )

    }
}