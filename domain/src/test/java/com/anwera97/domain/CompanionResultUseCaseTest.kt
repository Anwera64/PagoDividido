package com.anwera97.domain

import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.ResultModel
import com.anwera97.domain.repositories.CompanionRepository
import com.anwera97.domain.usecases.CompanionResultUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CompanionResultUseCaseTest {

    private val companionRepository: CompanionRepository = mock()
    private val companionResultUseCase: CompanionResultUseCase =
        CompanionResultUseCase(companionRepository)

    @Test
    fun `get result with only two companions`() = runTest {
        // Data
        val tripId = 1
        val payerId = 1
        val payerName = "Name 1"
        val payer = CompanionModel(payerId.toString(), payerName)
        val debtorName = "Name 2"
        val debtor = CompanionModel("", debtorName)
        val debtList = listOf(
            ResultModel(
                companion = payer,
                totalPaid = 0.0,
                debts = mutableMapOf(debtorName to 10.0),
                isExtended = false
            ),
            ResultModel(
                companion = debtor,
                totalPaid = 0.0,
                debts = mutableMapOf(payerName to 5.0),
                isExtended = false
            )
        )
        val expectedResult = ResultModel(
            companion = payer,
            totalPaid = 0.0,
            debts = mutableMapOf(debtorName to 5.0),
            isExtended = false
        )

        // Setup
        whenever(companionRepository.getResultInfoFor(tripId)) doReturn flow { emit(debtList) }

        // Exec
        val emittedResult = companionResultUseCase.getPayersWithDebtors(tripId, payerId).first()
        assertEquals(emittedResult, expectedResult)
    }

    @Test
    fun `get no result with wrong id`() = runTest {
        // Data
        val tripId = 1
        val payerId = 1
        val payer = CompanionModel("2", "")
        val debtList = listOf(
            ResultModel(
                companion = payer,
                totalPaid = 0.0,
                debts = mutableMapOf("" to 10.0),
                isExtended = false
            ),
        )

        // Setup
        whenever(companionRepository.getResultInfoFor(tripId)) doReturn flow { emit(debtList) }

        // Exec
        val emittedResult = companionResultUseCase.getPayersWithDebtors(tripId, payerId).first()
        assertNull(emittedResult)
    }

    @Test
    fun `get no result when trip emits no results`() = runTest {
        // Data
        val tripId = 1
        val payerId = 1
        val debtList: List<ResultModel> = emptyList()

        // Setup
        whenever(companionRepository.getResultInfoFor(tripId)) doReturn flow { emit(debtList) }

        // Exec
        val emittedResult = companionResultUseCase.getPayersWithDebtors(tripId, payerId).first()
        assertNull(emittedResult)
    }

    @Test
    fun `get result with three companions`() = runTest {
        // Data
        val tripId = 1
        val payerId = 1
        val payerName = "Name 1"
        val payer = CompanionModel(payerId.toString(), payerName)
        val firstDebtorName = "Name 2"
        val firstDebtor = CompanionModel("", firstDebtorName)
        val secondDebtorName = "Name 3"
        val secondDebtor = CompanionModel("", secondDebtorName)
        val debtList = listOf(
            ResultModel(
                companion = payer,
                totalPaid = 0.0,
                debts = mutableMapOf(
                    firstDebtorName to 10.0,
                    secondDebtorName to 5.0
                ),
                isExtended = false
            ),
            ResultModel(
                companion = firstDebtor,
                totalPaid = 0.0,
                debts = mutableMapOf(payerName to 5.0),
                isExtended = false
            ),
            ResultModel(
                companion = secondDebtor,
                totalPaid = 0.0,
                debts = mutableMapOf(payerName to 10.0),
                isExtended = false
            )
        )
        val expectedResult = ResultModel(
            companion = payer,
            totalPaid = 0.0,
            debts = mutableMapOf(
                firstDebtorName to 5.0,
                secondDebtorName to -5.0
            ),
            isExtended = false
        )

        // Setup
        whenever(companionRepository.getResultInfoFor(tripId)) doReturn flow { emit(debtList) }

        // Exec
        val emittedResult = companionResultUseCase.getPayersWithDebtors(tripId, payerId).first()
        assertEquals(emittedResult, expectedResult)
    }
}