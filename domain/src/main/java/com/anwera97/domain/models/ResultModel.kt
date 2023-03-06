package com.anwera97.domain.models

data class ResultModel(
    val companion: CompanionModel,
    val totalPaid: Double,
    val debts: MutableMap<String, Double>, // Name : Amount. Positive values represent debt.
    var isExtended: Boolean = false
)