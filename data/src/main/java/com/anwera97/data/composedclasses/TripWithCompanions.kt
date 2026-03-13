package com.anwera97.data.composedclasses

import androidx.room.Embedded
import androidx.room.Relation
import com.anwera97.data.entities.CompanionEntity
import com.anwera97.data.entities.Expenditure
import com.anwera97.data.entities.Trip

data class TripWithCompanions(
    @Embedded val trip: Trip,
    @Relation(
                parentColumn = "id",
                entityColumn = "trip_id",
        )
        val companions: List<CompanionEntity>,
    @Relation(
                parentColumn = "id",
                entityColumn = "trip_id"
        )
        val expenditures: List<Expenditure>
)
