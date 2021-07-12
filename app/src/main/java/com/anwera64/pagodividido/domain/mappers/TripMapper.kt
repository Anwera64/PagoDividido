package com.anwera64.pagodividido.domain.mappers

import com.anwera64.pagodividido.data.entities.Trip
import com.anwera64.pagodividido.domain.models.TripModel

object TripMapper {

    fun toModel(entity: Trip): TripModel = with(entity) {
        return TripModel(
            uid = id.toString(),
            name = name,
            totalSpent = 0.0f, //temp
            companions = hashMapOf(),
            expenditures = arrayListOf()
        )
    }
}