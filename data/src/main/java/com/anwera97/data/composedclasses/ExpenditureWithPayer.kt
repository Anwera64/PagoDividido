package com.anwera97.data.composedclasses

import androidx.room.Embedded
import androidx.room.Relation
import com.anwera97.data.entities.Companion
import com.anwera97.data.entities.Expenditure

data class ExpenditureWithPayer(
    @Embedded val expenditure: Expenditure,
    @Relation(
        parentColumn = "payer_id",
        entityColumn = "id"
    )
    val payer: Companion
)
