package com.anwera64.pagodividido.data.composedclasses

import androidx.room.Embedded
import androidx.room.Relation
import com.anwera64.pagodividido.data.entities.Companion
import com.anwera64.pagodividido.data.entities.Expenditure
import com.anwera64.pagodividido.data.entities.Trip

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
