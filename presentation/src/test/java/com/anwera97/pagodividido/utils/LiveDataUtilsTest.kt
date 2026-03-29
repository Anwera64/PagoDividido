package com.anwera97.pagodividido.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LiveDataUtilsTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `modifyLiveDataSet updates existing set`() {
        val liveData = MutableLiveData<Set<Int>>()
        liveData.value = setOf(1, 2)

        liveData.modifyLiveDataSet { add(3) }

        assertEquals(setOf(1, 2, 3), liveData.value)
    }

    @Test
    fun `modifyLiveDataSet initializes empty set when null`() {
        val liveData = MutableLiveData<Set<String>>()

        liveData.modifyLiveDataSet { add("a") }

        assertEquals(setOf("a"), liveData.value)
    }

    @Test
    fun `modifyLiveDataMap updates existing map`() {
        val liveData = MutableLiveData<Map<String, Int>>()
        liveData.value = mapOf("a" to 1)

        liveData.modifyLiveDataMap { put("b", 2) }

        assertEquals(2, liveData.value?.size)
        assertEquals(1, liveData.value?.get("a"))
        assertEquals(2, liveData.value?.get("b"))
    }

    @Test
    fun `modifyLiveDataMap initializes empty map when null`() {
        val liveData = MutableLiveData<Map<String, Int>>()

        liveData.modifyLiveDataMap { put("x", 9) }

        assertTrue(liveData.value?.containsKey("x") == true)
        assertEquals(9, liveData.value?.get("x"))
    }
}

