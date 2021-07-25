package com.anwera64.pagodividido.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
        primaryKeys = ["companion_id", "expenditure_id"],
        foreignKeys = [ForeignKey(
                entity = Companion::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("companion_id"),
                onDelete = ForeignKey.CASCADE
        ), ForeignKey(
                entity = Expenditure::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("expenditure_id"),
                onDelete = ForeignKey.CASCADE
        )]
)
data class Debtors(
        @ColumnInfo(name = "companion_id") val companionId: Int,
        @ColumnInfo(name = "expenditure_id") val expenditureId: Int,
        @ColumnInfo(name = "amount") val amount: Double
)
