package com.anwera64.pagodividido.domain.models

import java.util.*

class ExpenditureModel(
    val uid: String,
    val payer: CompanionModel,
    val debtors: ArrayList<CompanionModel>,
    val detail: String,
    val amountSpent: Float,
    val date: Date
)