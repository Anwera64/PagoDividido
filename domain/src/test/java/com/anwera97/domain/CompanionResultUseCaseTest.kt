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
}