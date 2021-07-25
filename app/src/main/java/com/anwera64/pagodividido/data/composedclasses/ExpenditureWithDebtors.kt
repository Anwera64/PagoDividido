package com.anwera64.pagodividido.data.composedclasses

import androidx.room.Embedded
import androidx.room.Relation
import com.anwera64.pagodividido.data.entities.Debtors
import com.anwera64.pagodividido.data.entities.Expenditure

class ExpenditureWithDebtors(
        @Embedded
        val expenditure: Expenditure,
        @Relation(
                entity = Debtors::class,
                parentColumn = "id",
                entityColumn = "expenditure_id"
        )
        val debtors: List<DebtWithCompanion>
)