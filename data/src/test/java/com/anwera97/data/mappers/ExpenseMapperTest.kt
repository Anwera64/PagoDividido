package com.anwera97.data.mappers

import com.anwera97.data.composedclasses.ExpenditureWithDebtorsAndPayer
import com.anwera97.data.entities.CompanionEntity
import com.anwera97.data.entities.Expenditure
import com.anwera97.domain.models.ExpenseCreationData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Date

class ExpenseMapperTest {

    @Test
    fun `toEntity maps expense creation data`() {
        val date = Date(100)
        val input = ExpenseCreationData(
            id = 12,
            expense = 33.0,
            date = date,
            tripId = 4,
            payerId = 8,
            detail = "Dinner"
        )

        val entity = ExpenseMapper.toEntity(input)

        assertEquals(12, entity.id)
        assertEquals(33.0, entity.expense, 0.0)
        assertNull(entity.imageRef)
        assertEquals(date, entity.date)
        assertEquals(4, entity.tripId)
        assertEquals(8, entity.payerId)
        assertEquals("Dinner", entity.detail)
    }

    @Test
    fun `toModel maps expenditure composite`() {
        val date = Date(200)
        val entity = ExpenditureWithDebtorsAndPayer(
            expenditure = Expenditure(
                id = 77,
                expense = 14.5,
                imageRef = null,
                date = date,
                tripId = 1,
                payerId = 10,
                detail = null
            ),
            debtors = listOf(
                CompanionEntity(name = "Bob", tripId = 1, id = 20),
                CompanionEntity(name = "Carol", tripId = 1, id = 21)
            ),
            payer = CompanionEntity(name = "Ana", tripId = 1, id = 10)
        )

        val model = ExpenseMapper.toModel(entity)

        assertEquals("77", model.uid)
        assertEquals("Ana", model.payer.name)
        assertEquals(2, model.debtors.size)
        assertEquals("Bob", model.debtors[0].name)
        assertEquals(14.5, model.amountSpent, 0.0)
        assertNull(model.detail)
        assertEquals(date, model.date)
    }
}

