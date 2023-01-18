package com.anwera97.domain.models

class ResultModel(
    val companion: CompanionModel,
    val totalPaid: Double,
    val debts: MutableMap<String, Double>, // Name : Amount. Positive values represent debt.
    var isExtended: Boolean = false
) {
    fun clone(): ResultModel {
        val copyOfDebts = debts.map { entry -> entry.key to entry.value }.toMap()
        return ResultModel(companion, totalPaid, HashMap(copyOfDebts))
    }
}