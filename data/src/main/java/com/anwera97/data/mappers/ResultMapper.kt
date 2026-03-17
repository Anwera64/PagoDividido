package com.anwera97.data.mappers

import com.anwera97.data.composedclasses.AggregatedDebt
import com.anwera97.data.entities.CompanionEntity
import com.anwera97.domain.models.ResultModel

object ResultMapper {

    fun toModel(aggregations: Map<CompanionEntity, List<AggregatedDebt>>): List<ResultModel> {
        return aggregations.map { (payer, payerDebts) ->
            ResultModel(
                companion = CompanionMapper.toModel(payer),
                totalPaid = payerDebts.sumOf { it.amount },
                debts = payerDebts
                    .filter { it.debtorName != payer.name }
                    .associate { it.debtorName to it.amount }
                    .toMutableMap()
            )
        }
    }
}
