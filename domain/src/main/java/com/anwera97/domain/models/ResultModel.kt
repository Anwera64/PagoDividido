package com.anwera97.domain.models

data class ResultModel(
    val companion: CompanionModel,
    val totalPaid: Double,
    val debts: MutableMap<CompanionModel, Double>, // Key changed to CompanionModel
    val isExtended: Boolean = false
)
