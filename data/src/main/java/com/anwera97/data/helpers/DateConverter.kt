package com.anwera97.data.helpers

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let(::Date)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}