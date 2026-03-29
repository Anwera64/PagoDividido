package com.anwera97.data.mappers

import com.anwera97.data.composedclasses.DebtWithCompanion
import com.anwera97.data.composedclasses.ExpenditureWithDebtors
import com.anwera97.data.composedclasses.PayerWithExpendituresAndDebtors
import com.anwera97.data.entities.CompanionEntity
import com.anwera97.data.entities.Debtors
import com.anwera97.data.entities.Expenditure
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.Date

class CompanionMapperTest {

    @Test
    fun `toModel maps companion entity`() {
        val entity = CompanionEntity(name = "Ana", tripId = 2, id = 5)

        val result = CompanionMapper.toModel(entity)

        assertEquals("5", result.uid)
        assertEquals("Ana", result.name)
    }

    @Test
    fun `toModel maps payer totals and debtor aggregation`() {
        val payer = CompanionEntity(name = "Ana", tripId = 1, id = 1)
        val bob = CompanionEntity(name = "Bob", tripId = 1, id = 2)
        val carol = CompanionEntity(name = "Carol", tripId = 1, id = 3)

        val firstExpense = ExpenditureWithDebtors(
            expenditure = Expenditure(id = 10, expense = 50.0, imageRef = null, date = Date(0), tripId = 1, payerId = 1, detail = null),
            debtors = listOf(
                DebtWithCompanion(Debtors(companionId = 2, expenditureId = 10, amount = 30.0), bob),
                DebtWithCompanion(Debtors(companionId = 1, expenditureId = 10, amount = 5.0), payer)
            )
        )

        val secondExpense = ExpenditureWithDebtors(
            expenditure = Expenditure(id = 11, expense = 30.0, imageRef = null, date = Date(1), tripId = 1, payerId = 1, detail = null),
            debtors = listOf(
                DebtWithCompanion(Debtors(companionId = 2, expenditureId = 11, amount = 20.0), bob),
                DebtWithCompanion(Debtors(companionId = 3, expenditureId = 11, amount = 10.0), carol)
            )
        )

        val composite = PayerWithExpendituresAndDebtors(
            payer = payer,
            expenditures = listOf(firstExpense, secondExpense)
        )

        val result = CompanionMapper.toModel(composite)

        assertEquals("Ana", result.companion.name)
        assertEquals(65.0, result.totalPaid, 0.0)
        assertEquals(50.0, result.debts["Bob"] ?: 0.0, 0.0)
        assertEquals(10.0, result.debts["Carol"] ?: 0.0, 0.0)
        assertFalse(result.debts.containsKey("Ana"))
    }
}

