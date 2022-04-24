package com.anwera97.data.composedclasses

import androidx.room.Embedded
import androidx.room.Relation
import com.anwera97.data.entities.Companion
import com.anwera97.data.entities.Expenditure
import com.anwera97.data.entities.Trip

data class TripWithCompanions(
    @Embedded val trip: Trip,
    @Relation(
                parentColumn = "id",
                entityColumn = "trip_id",
        )
        val companions: List<Companion>,
    @Relation(
                parentColumn = "id",
                entityColumn = "trip_id"
        )
        val expenditures: List<Expenditure>
)
