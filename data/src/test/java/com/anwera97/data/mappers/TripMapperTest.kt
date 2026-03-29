package com.anwera97.data.mappers

import com.anwera97.data.composedclasses.TripWithCompanions
import com.anwera97.data.entities.CompanionEntity
import com.anwera97.data.entities.Expenditure
import com.anwera97.data.entities.Trip
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class TripMapperTest {

    @Test
    fun `toModel maps trip companions and total spent`() {
        val entity = TripWithCompanions(
            trip = Trip(name = "Road trip", id = 4),
            companions = listOf(
                CompanionEntity(name = "Ana", tripId = 4, id = 10),
                CompanionEntity(name = "Bob", tripId = 4, id = 11)
            ),
            expenditures = listOf(
                Expenditure(expense = 12.5, imageRef = null, date = Date(0), tripId = 4, payerId = 10, detail = null),
                Expenditure(expense = 7.5, imageRef = null, date = Date(1), tripId = 4, payerId = 11, detail = "snack")
            )
        )

        val model = TripMapper.toModel(entity)

        assertEquals("4", model.uid)
        assertEquals("Road trip", model.name)
        assertEquals(20.0, model.totalSpent, 0.0)
        assertEquals(2, model.companions.size)
        assertEquals("Ana", model.companions[0].name)
        assertEquals("11", model.companions[1].uid)
    }
}

