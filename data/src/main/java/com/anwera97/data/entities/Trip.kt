package com.anwera97.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip(
        @ColumnInfo(name = "name") val name: String,
        @PrimaryKey(autoGenerate = true) val id: Int = 0
)
