package com.anwera97.data.mappers

import com.anwera97.data.entities.CompanionEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class CompanionMapperTest {

    @Test
    fun `toModel maps companion entity`() {
        val entity = CompanionEntity(name = "Ana", tripId = 2, id = 5)

        val result = CompanionMapper.toModel(entity)

        assertEquals("5", result.uid)
        assertEquals("Ana", result.name)
    }
}
