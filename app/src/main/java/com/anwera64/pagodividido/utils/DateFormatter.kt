package com.anwera64.pagodividido.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    const val DEFAULT_FORMAT = "dd/MM/yyyy"

    fun formatDate(date: Date, format: String = DEFAULT_FORMAT): String {
        val df = SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault())
        return df.format(date)
    }
}