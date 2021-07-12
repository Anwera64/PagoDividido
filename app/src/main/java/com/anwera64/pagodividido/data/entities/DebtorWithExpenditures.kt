package com.anwera64.pagodividido.data.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DebtorWithExpenditures(
    @Embedded val debtor: Companion,
    @Relation(
        parentColumn = "id",
        entityColumn = "expenditure_id",
        associateBy = Junction(Debtors::class)
    )
    val expenditures: List<Expenditure>
)
