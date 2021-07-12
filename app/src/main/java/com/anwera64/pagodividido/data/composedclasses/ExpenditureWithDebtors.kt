package com.anwera64.pagodividido.data.composedclasses

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.anwera64.pagodividido.data.entities.Companion
import com.anwera64.pagodividido.data.entities.Debtors
import com.anwera64.pagodividido.data.entities.Expenditure

data class ExpenditureWithDebtors(
    @Embedded val expenditure: Expenditure,
    @Relation(
        parentColumn = "id",
        entityColumn = "companion_id",
        associateBy = Junction(Debtors::class)
    )
    val debtors: List<Companion>
)
