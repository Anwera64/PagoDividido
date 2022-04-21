package com.anwera97.data.composedclasses

import androidx.room.Embedded
import androidx.room.Relation
import com.anwera97.data.entities.Companion
import com.anwera97.data.entities.Expenditure

data class PayerWithExpendituresAndDebtors(
    @Embedded val payer: Companion,
    @Relation(
                entity = Expenditure::class,
                parentColumn = "id",
                entityColumn = "payer_id"
        )
        val expenditures: List<ExpenditureWithDebtors>
)