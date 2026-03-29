package com.anwera97.domain

import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.domain.repositories.ExpenditureRepository
import com.anwera97.domain.usecases.ExpenditureUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenditureUseCaseTest {

    private val expenditureRepository: ExpenditureRepository = mock()
    private val expenditureUseCase = ExpenditureUseCase(expenditureRepository)

    @Test
    fun `get expenditures delegates to repository`() = runTest {
        val payer = CompanionModel(uid = "1", name = "Ana")
        val debtors = listOf(CompanionModel(uid = "2", name = "Bob"))
        val expenditures = listOf(
            ExpenditureModel(
                uid = "77",
                payer = payer,
                debtors = debtors,
                detail = "Lunch",
                amountSpent = 25.0,
                date = Date(0)
            )
        )
        whenever(expenditureRepository.getExpense(9)) doReturn flowOf(expenditures)

        val result = expenditureUseCase.getExpenditures(9).first()

        assertEquals(1, result.size)
        assertEquals("77", result[0].uid)
        assertEquals("Ana", result[0].payer.name)
        assertEquals(25.0, result[0].amountSpent, 0.0)
        assertEquals("Lunch", result[0].detail)
    }
}

