package com.anwera97.data.composedclasses

import androidx.room.Embedded
import androidx.room.Relation
import com.anwera97.data.entities.Companion
import com.anwera97.data.entities.Debtors
import com.anwera97.data.entities.Expenditure

class ExpenditureWithDebtorsAndPayer(
    @Embedded
    val expenditure: Expenditure,
    @Relation(
        entity = Debtors::class,
        parentColumn = "id",
        entityColumn = "expenditure_id"
    )
    val debtors: List<DebtWithCompanion>,
    @Relation(
        parentColumn = "payer_id",
        entityColumn = "id"
    )
    val payer: Companion
)