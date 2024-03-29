package com.anwera64.pagodividido.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    private const val DEFAULT_FORMAT = "MMM dd"

    @JvmStatic
    @JvmOverloads
    fun formatDate(date: Date, format: String = DEFAULT_FORMAT): String {
        val df = SimpleDateFormat(format, Locale.getDefault())
        return df.format(date)
    }
}