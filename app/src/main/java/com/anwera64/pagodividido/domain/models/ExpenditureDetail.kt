package com.anwera64.pagodividido.domain.models

import java.util.*

class ExpenditureDetail(
    val uid: String,
    val payer: CompanionModel,
    val debtors: List<Debtor>,
    val detail: String?,
    val amountSpent: Double,
    val date: Date
)