package com.anwera64.pagodividido.data.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ExpenditureWithDebtors(
    @Embedded val expenditure: Expenditure,
    @Relation(
        parentColumn = "id",
        entityColumn = "companion_id",
        associateBy = Junction(Debtors::class)
    )
    val debtors: List<Companion>
)
