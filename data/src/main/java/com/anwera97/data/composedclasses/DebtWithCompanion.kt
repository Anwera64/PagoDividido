package com.anwera97.data.composedclasses

import androidx.room.Embedded
import androidx.room.Relation
import com.anwera97.data.entities.Companion
import com.anwera97.data.entities.Debtors

class DebtWithCompanion(
    @Embedded val debtors: Debtors,
    @Relation(
                parentColumn = "companion_id",
                entityColumn = "id"
        )
        val companion: Companion
)