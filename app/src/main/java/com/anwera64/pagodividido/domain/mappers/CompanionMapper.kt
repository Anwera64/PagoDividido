package com.anwera64.pagodividido.domain.mappers

import com.anwera64.pagodividido.data.composedclasses.PayerWithExpendituresAndDebtors
import com.anwera64.pagodividido.data.entities.Companion
import com.anwera64.pagodividido.domain.models.CompanionModel
import com.anwera64.pagodividido.domain.models.ResultModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.exp

object CompanionMapper {

    fun toModel(companionEntity: Companion): CompanionModel = with(companionEntity) {
        return CompanionModel(uid = id.toString(), name = name)
    }

    fun toModel(payerComposite: PayerWithExpendituresAndDebtors): ResultModel =
        with(payerComposite) {
            return ResultModel(
                companion = toModel(payer),
                totalPayed = getTotalPayment(),
                debts = getDebts()
            )
        }

    fun makeCalculation(results: List<ResultModel>): List<ResultModel> {
        val resultMap = toHashMap(results.map(ResultModel::clone))
        results.forEachIndexed() { index, result ->
            result.debts.entries.forEach { entry ->
                //key -> name, value -> amount
                val amount = resultMap[entry.key]?.debts?.get(result.companion.name) ?: 0.0
                results[index].debts[entry.key] = (result.debts[entry.key] ?: 0.0) - amount
            }
        }
        return results
    }
}

private fun toHashMap(results: List<ResultModel>): Map<String, ResultModel> {
    val map = HashMap<String, ResultModel>()
    results.forEach { resultModel -> map[resultModel.companion.name] = resultModel }
    return map
}

private fun PayerWithExpendituresAndDebtors.getTotalPayment(): Double {
    var totalAmount = 0.0
    expenditures.forEach { expenditure ->
        expenditure.debtors.forEach { debtor ->
            totalAmount += debtor.debtors.amount
        }
    }
    return totalAmount
}

private fun PayerWithExpendituresAndDebtors.getDebts(): HashMap<String, Double> {
    val map: HashMap<String, Double> = HashMap()
    expenditures.forEach { expenditure ->
        for (debtor in expenditure.debtors) {
            if (debtor.companion.name == payer.name) continue
            map[debtor.companion.name] = (map[debtor.companion.name] ?: 0.0) + debtor.debtors.amount
        }
    }
    return map
}