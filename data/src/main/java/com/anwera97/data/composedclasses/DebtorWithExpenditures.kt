package com.anwera97.data.composedclasses

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.anwera97.data.entities.Companion
import com.anwera97.data.entities.Debtors
import com.anwera97.data.entities.Expenditure

data class DebtorWithExpenditures(
    @Embedded val debtor: Companion,
    @Relation(
                parentColumn = "id",
                entityColumn = "expenditure_id",
                associateBy = Junction(Debtors::class)
        )
        val expenditures: List<Expenditure>
)
