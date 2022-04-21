package com.anwera97.data.composedclasses

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.anwera97.data.entities.Companion
import com.anwera97.data.entities.Debtors
import com.anwera97.data.entities.Expenditure

data class ExpenditureWithDebtorsAndPayer(
    @Embedded val expenditure: Expenditure,
    @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        Debtors::class,
                        parentColumn = "expenditure_id",
                        entityColumn = "companion_id"
                )
        )
        val debtors: List<Companion>,
    @Relation(
                parentColumn = "payer_id",
                entityColumn = "id"
        )
        val payer: Companion
)
