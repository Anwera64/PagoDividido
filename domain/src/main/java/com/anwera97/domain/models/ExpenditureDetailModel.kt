package com.anwera97.domain.models

import java.util.*

class ExpenditureDetailModel(
    val uid: String,
    val payer: CompanionModel,
    val detail: String?,
    val amountSpent: Double,
    val date: Date,
    val debtors: List<DebtorModel>
)