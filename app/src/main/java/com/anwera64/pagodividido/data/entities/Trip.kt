package com.anwera64.pagodividido.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String
)
