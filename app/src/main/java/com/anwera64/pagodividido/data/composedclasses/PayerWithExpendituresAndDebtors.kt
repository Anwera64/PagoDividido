package com.anwera64.pagodividido.data.composedclasses

import androidx.room.Embedded
import androidx.room.Relation
import com.anwera64.pagodividido.data.entities.Companion
import com.anwera64.pagodividido.data.entities.Expenditure

data class PayerWithExpendituresAndDebtors(
        @Embedded val payer: Companion,
        @Relation(
                entity = Expenditure::class,
                parentColumn = "id",
                entityColumn = "payer_id"
        )
        val expenditures: List<ExpenditureWithDebtors>
)