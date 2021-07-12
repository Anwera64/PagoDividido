package com.anwera64.pagodividido.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("trip_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Companion::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("payer"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class Expenditure(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "expense") val expense: Double,
    @ColumnInfo(name = "image_ref") val imageRef: String,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "trip_id") val tripId: String,
    @ColumnInfo(name = "payer") val payer: String
)
