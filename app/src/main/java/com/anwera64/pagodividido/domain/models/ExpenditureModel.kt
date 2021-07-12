package com.anwera64.pagodividido.domain.models

import java.util.*

class ExpenditureModel(
    val uid: String,
    val payer: CompanionModel,
    val debtors: List<CompanionModel>,
    val detail: String?,
    val amountSpent: Double,
    val date: Date
)