package com.anwera97.data.helpers

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Date

class DateConverterTest {

    @Test
    fun `toDate returns date when input is not null`() {
        val millis = 1234L

        val result = DateConverter.toDate(millis)

        assertEquals(Date(millis), result)
    }

    @Test
    fun `toDate returns null when input is null`() {
        assertNull(DateConverter.toDate(null))
    }

    @Test
    fun `fromDate returns millis when date is not null`() {
        val date = Date(5678L)

        val result = DateConverter.fromDate(date)

        assertEquals(5678L, result)
    }

    @Test
    fun `fromDate returns null when date is null`() {
        assertNull(DateConverter.fromDate(null))
    }
}

