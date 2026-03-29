package com.anwera97.pagodividido.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EventWrapperTest {

    @Test
    fun `getContentIfHandled returns value once and then null`() {
        val wrapper = EventWrapper("payload")

        val firstRead = wrapper.getContentIfHandled()
        val secondRead = wrapper.getContentIfHandled()

        assertEquals("payload", firstRead)
        assertNull(secondRead)
        assertTrue(wrapper.hasBeenHandled)
    }

    @Test
    fun `peekContent does not mark event handled`() {
        val wrapper = EventWrapper(42)

        val peeked = wrapper.peekContent()

        assertEquals(42, peeked)
        assertFalse(wrapper.hasBeenHandled)
    }

    @Test
    fun `nullOrHandled returns expected values`() {
        val nullWrapper: EventWrapper<String>? = null
        val pendingWrapper = EventWrapper("x")
        val handledWrapper = EventWrapper("y").also { it.getContentIfHandled() }

        assertTrue(nullWrapper.nullOrHandled())
        assertFalse(pendingWrapper.nullOrHandled())
        assertTrue(handledWrapper.nullOrHandled())
    }
}

