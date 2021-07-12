package com.anwera64.pagodividido.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("trip_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Companion(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "trip_id") val tripId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
