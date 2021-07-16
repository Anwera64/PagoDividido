package com.anwera64.pagodividido.data.composedclasses

import androidx.room.Embedded
import androidx.room.Relation
import com.anwera64.pagodividido.data.entities.Companion
import com.anwera64.pagodividido.data.entities.Debtors

class DebtWithCompanion(
    @Embedded val debtors: Debtors,
    @Relation(
        parentColumn = "companion_id",
        entityColumn = "id"
    )
    val companion: Companion
)