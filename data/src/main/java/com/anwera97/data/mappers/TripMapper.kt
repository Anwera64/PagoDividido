package com.anwera97.data.mappers

import com.anwera97.data.composedclasses.TripWithCompanions
import com.anwera97.data.entities.Expenditure
import com.anwera97.domain.models.TripModel

private fun getTotalSpent(expenditures: List<Expenditure>): Double {
    var amount = 0.0
    expenditures.forEach { expenditure ->
        amount += expenditure.expense
    }
    return amount
}

object TripMapper {

    fun toModel(entity: TripWithCompanions): TripModel = with(entity) {
        return TripModel(
            uid = trip.id.toString(),
            name = trip.name,
            totalSpent = getTotalSpent(expenditures),
            companions = companions.map(CompanionMapper::toModel)
        )
    }
}