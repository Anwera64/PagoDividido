package com.anwera64.pagodividido.domain.models

class ResultModel(
        val companion: CompanionModel,
        val totalPayed: Double,
        val debts: HashMap<String, Double>,
        var isExtended: Boolean = false
) {
    fun clone(): ResultModel {
        val copyOfDebts = debts.map { entry -> entry.key to entry.value }.toMap()
        return ResultModel(companion, totalPayed, HashMap(copyOfDebts))
    }
}