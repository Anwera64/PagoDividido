package com.anwera64.pagodividido.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["companion_id", "expenditure_id"])
data class Debtors(
    @ColumnInfo(name = "companion_id") val companionId: String,
    @ColumnInfo(name = "expenditure_id") val expenditureId: String
)
