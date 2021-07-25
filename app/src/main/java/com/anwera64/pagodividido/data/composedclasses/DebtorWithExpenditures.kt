package com.anwera64.pagodividido.data.composedclasses

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.anwera64.pagodividido.data.entities.Companion
import com.anwera64.pagodividido.data.entities.Debtors
import com.anwera64.pagodividido.data.entities.Expenditure

data class DebtorWithExpenditures(
        @Embedded val debtor: Companion,
        @Relation(
                parentColumn = "id",
                entityColumn = "expenditure_id",
                associateBy = Junction(Debtors::class)
        )
        val expenditures: List<Expenditure>
)
