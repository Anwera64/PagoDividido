package com.anwera97.domain

import com.anwera97.domain.models.*
import com.anwera97.domain.repositories.ExpenditureRepository
import com.anwera97.domain.usecases.NewExpenditureUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class NewExpenseUseCaseTest {

    private val expenditureRepository: ExpenditureRepository = mock()
    private val newExpenditureUseCase = NewExpenditureUseCase(expenditureRepository)

    @Test
    fun `add expenditure with detail`() = runTest {
        val id = 1
        val debtors = emptyMap<Int, Double>()
        val detail = "detail"
        val amountSpent = 10.0
        val expectedExpenseData = ExpenseCreationData(
            expense = amountSpent,
            date = Date(),
            tripId = id,
            payerId = id,
            detail = detail
        )

        newExpenditureUseCase.addExpenditure(
            tripId = id,
            payerId = id,
            debtors = debtors,
            detail = detail,
            amountSpent = amountSpent
        )

        verify(expenditureRepository).addExpense(expectedExpenseData, debtors)
    }

    @Test
    fun `add expenditure with empty detail`() = runTest {
        val id = 1
        val debtors = emptyMap<Int, Double>()
        val detail = ""
        val amountSpent = 10.0
        val expectedExpenseData = ExpenseCreationData(
            expense = amountSpent,
            date = Date(),
            tripId = id,
            payerId = id,
            detail = null
        )

        newExpenditureUseCase.addExpenditure(
            tripId = id,
            payerId = id,
            debtors = debtors,
            detail = detail,
            amountSpent = amountSpent
        )

        verify(expenditureRepository).addExpense(expectedExpenseData, debtors)
    }

    @Test
    fun `add expenditure with null detail`() = runTest {
        val id = 1
        val debtors = emptyMap<Int, Double>()
        val detail = null
        val amountSpent = 10.0
        val expectedExpenseData = ExpenseCreationData(
            expense = amountSpent,
            date = Date(),
            tripId = id,
            payerId = id,
            detail = null
        )

        newExpenditureUseCase.addExpenditure(
            tripId = id,
            payerId = id,
            debtors = debtors,
            detail = detail,
            amountSpent = amountSpent
        )

        verify(expenditureRepository).addExpense(expectedExpenseData, debtors)
    }

    @Test
    fun `look for debtor input errors green path`() {
        val totalAmount = 100.0
        val debtors = mapOf(
            1 to 10.0,
            2 to 20.0,
            3 to 70.0
        )

        val result = newExpenditureUseCase.lookForDebtorInputErrors(totalAmount, debtors)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `look for debtor input errors when values contain 0 or negative`() {
        val totalAmount = 100.0
        val debtors = mapOf(
            1 to 0.0,
            2 to -20.0,
            3 to 70.0
        )
        val zeroError = DebtorInputError(1, DebtorInputErrorReasons.ZERO_OR_NEGATIVE)
        val negativeError = DebtorInputError(2, DebtorInputErrorReasons.ZERO_OR_NEGATIVE)

        val result = newExpenditureUseCase.lookForDebtorInputErrors(totalAmount, debtors)
        assertTrue(result.contains(zeroError))
        assertTrue(result.contains(negativeError))
    }

    @Test
    fun `look for debtor input errors when there are values over the total amount`() {
        val totalAmount = 100.0
        val debtors = mapOf(
            1 to 101.0,
            2 to 1.0,
            3 to 2.0
        )
        val maxAmountError = DebtorInputError(1, DebtorInputErrorReasons.OVER_MAX_AMOUNT)

        val result = newExpenditureUseCase.lookForDebtorInputErrors(totalAmount, debtors)
        assertTrue(result.contains(maxAmountError))
    }

    @Test
    fun `look for debtor input errors when the sum is over the total amount`() {
        val totalAmount = 100.0
        val debtors = mapOf(
            1 to 50.0,
            2 to 50.0,
            3 to 2.0
        )
        val overMaxAmountError = DebtorInputError(3, DebtorInputErrorReasons.SUM_EXCEEDS_MAX_AMOUNT)

        val result = newExpenditureUseCase.lookForDebtorInputErrors(totalAmount, debtors)
        assertTrue(result.contains(overMaxAmountError))
    }

    @Test
    fun `debts equal the total amount`() {
        val totalAmount = 100.0
        val debtors = mapOf(
            1 to 25.0,
            2 to 50.0,
            3 to 25.0
        )
        val expected = 0.0

        val result = newExpenditureUseCase.checkAmountsDifference(totalAmount, debtors)

        assertEquals(result, expected, 0.0)
    }

    @Test
    fun `debts below the total amount`() {
        val totalAmount = 100.0
        val debtors = mapOf(
            1 to 25.0,
            2 to 25.0,
            3 to 25.0
        )
        val expected = 25.0

        val result = newExpenditureUseCase.checkAmountsDifference(totalAmount, debtors)

        assertEquals(result, expected, 0.0)
    }

    @Test
    fun `check amount input error green path`() {
        val amountInput = "20"
        val expected = InputErrorType.NO_ERROR
        val result = newExpenditureUseCase.checkAmountInputError(amountInput)
        assertEquals(expected, result)
    }

    @Test
    fun `check amount input error number with decimals`() {
        val amountInput = "20.65468"
        val expected = InputErrorType.NO_ERROR
        val result = newExpenditureUseCase.checkAmountInputError(amountInput)
        assertEquals(expected, result)
    }

    @Test
    fun `check amount input error empty field`() {
        val amountInput = ""
        val expected = InputErrorType.INPUT_AMOUNT
        val result = newExpenditureUseCase.checkAmountInputError(amountInput)
        assertEquals(expected, result)
    }

    @Test
    fun `check amount input error blank field`() {
        val amountInput = " "
        val expected = InputErrorType.INPUT_NUMBER
        val result = newExpenditureUseCase.checkAmountInputError(amountInput)
        assertEquals(expected, result)
    }

    @Test
    fun `check amount input error not a number field`() {
        val amountInput = "word"
        val expected = InputErrorType.INPUT_NUMBER
        val result = newExpenditureUseCase.checkAmountInputError(amountInput)
        assertEquals(expected, result)
    }

    @Test
    fun `check payment option input error when it exists`() {
        val exists = true
        val expected = InputErrorType.NO_ERROR
        val result = newExpenditureUseCase.checkPaymentOptionError(exists)
        assertEquals(expected, result)
    }

    @Test
    fun `check payment option input error when it doesn't exist`() {
        val exists = false
        val expected = InputErrorType.MISSING_FIELD
        val result = newExpenditureUseCase.checkPaymentOptionError(exists)
        assertEquals(expected, result)
    }

    @Test
    fun `check payer input error green path`() {
        val id = 1
        val expected = InputErrorType.NO_ERROR
        val result = newExpenditureUseCase.checkPayerInputError(id)
        assertEquals(expected, result)
    }

    @Test
    fun `check payer input error null`() {
        val id = null
        val expected = InputErrorType.MISSING_FIELD
        val result = newExpenditureUseCase.checkPayerInputError(id)
        assertEquals(expected, result)
    }

    @Test
    fun `check payer input error inexistent`() {
        val id = -1
        val expected = InputErrorType.INEXISTENT_ID
        val result = newExpenditureUseCase.checkPayerInputError(id)
        assertEquals(expected, result)
    }

    @Test
    fun `create equal payments green path`() {
        val companions = listOf(
            CompanionModel("1", ""),
            CompanionModel("2", "")
        )
        val amountSpent = 100.0
        val expectedResult = mapOf(
            1 to 50.0,
            2 to 50.0
        )

        val result = newExpenditureUseCase.createEqualPayments(companions, amountSpent)

        assertEquals(result, expectedResult)
    }

    @Test
    fun `create equal payments total amount 0`() {
        val companions = listOf(
            CompanionModel("1", ""),
            CompanionModel("2", "")
        )
        val amountSpent = 0.0
        val expectedResult = mapOf(
            1 to 0.0,
            2 to 0.0
        )

        val result = newExpenditureUseCase.createEqualPayments(companions, amountSpent)

        assertEquals(result, expectedResult)
    }

    @Test
    fun `create equal payments no companions`() {
        val companions = emptyList<CompanionModel>()
        val amountSpent = 0.0
        val expectedResult = emptyMap<Int, Double>()

        val result = newExpenditureUseCase.createEqualPayments(companions, amountSpent)

        assertEquals(result, expectedResult)
    }

    @Test
    fun `complete debt map green path`() {
        val paymentRelationship = mapOf(
            1 to 50.0
        )
        val companions = listOf(
            CompanionModel("1", ""),
            CompanionModel("2", "")
        )
        val expectedResult = mapOf(
            1 to 50.0,
            2 to 0.0
        )

        val result = newExpenditureUseCase.completeDebtMap(paymentRelationship, companions)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `complete debt map and already complete map`() {
        val paymentRelationship = mapOf(
            1 to 50.0,
            2 to 40.0
        )
        val companions = listOf(
            CompanionModel("1", ""),
            CompanionModel("2", "")
        )
        val expectedResult = mapOf(
            1 to 50.0,
            2 to 40.0
        )

        val result = newExpenditureUseCase.completeDebtMap(paymentRelationship, companions)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `complete debt map and empty map`() {
        val paymentRelationship = emptyMap<Int, Double>()
        val companions = listOf(
            CompanionModel("1", ""),
            CompanionModel("2", "")
        )
        val expectedResult = mapOf(
            1 to 0.0,
            2 to 0.0
        )

        val result = newExpenditureUseCase.completeDebtMap(paymentRelationship, companions)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `complete debt map and with no companions`() {
        val paymentRelationship = emptyMap<Int, Double>()
        val companions = emptyList<CompanionModel>()
        val expectedResult = emptyMap<Int, Double>()

        val result = newExpenditureUseCase.completeDebtMap(paymentRelationship, companions)

        assertEquals(expectedResult, result)
    }
}