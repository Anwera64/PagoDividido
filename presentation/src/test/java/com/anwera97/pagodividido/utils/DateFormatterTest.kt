package com.anwera97.pagodividido.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class DateFormatterTest {

    @Test
    fun `formatDate uses custom format`() {
        val date = Date(0)

        val formatted = DateFormatter.formatDate(date, "yyyy")

        assertEquals("1970", formatted)
    }

    @Test
    fun `formatDate returns non-empty with default format`() {
        val date = Date(0)

        val formatted = DateFormatter.formatDate(date)

        assertEquals(6, formatted.length)
    }
}

