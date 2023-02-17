package com.anwera97.domain

import com.anwera97.domain.models.TripShortModel
import com.anwera97.domain.repositories.CompanionRepository
import com.anwera97.domain.repositories.TripRepository
import com.anwera97.domain.usecases.CreateTripUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CreateTripUseCaseTest {

    private val tripRepository: TripRepository = mock()
    private val companionRepository: CompanionRepository = mock()

    private val createTripUseCase = CreateTripUseCase(tripRepository, companionRepository)

    @Test
    fun `check companion size green path`() {
        val companions: List<String> = listOf("Name 1", "Name 2", "Name 3")
        val result = createTripUseCase.checkCompanionsSize(companions)
        assertTrue(result)
    }

    @Test
    fun `check companion size empty list`() {
        val companions: List<String> = emptyList()
        val result = createTripUseCase.checkCompanionsSize(companions)
        assertFalse(result)
    }

    @Test
    fun `check companion size size below required`() {
        val companions: List<String> = listOf("Name 1")
        val result = createTripUseCase.checkCompanionsSize(companions)
        assertFalse(result)
    }

    @Test
    fun `check trip name green path`() {
        val tripName = "Trip Name"
        val result = createTripUseCase.checkTripName(tripName)
        assertTrue(result)
    }

    @Test
    fun `check trip name empty`() {
        val tripName = ""
        val result = createTripUseCase.checkTripName(tripName)
        assertFalse(result)
    }

    @Test
    fun `check trip name blank`() {
        val tripName = " "
        val result = createTripUseCase.checkTripName(tripName)
        assertFalse(result)
    }

    @Test
    fun `create trip`() = runTest {
        val companions: List<String> = listOf("Name 1", "Name 2", "Name 3")
        val tripName = "Trip Name"
        val tripId = 1L
        val expectedResult = TripShortModel(tripName, tripId.toInt())

        whenever(tripRepository.insert(tripName)) doReturn tripId
        val result = createTripUseCase.createTrip(companions, tripName)

        assertEquals(expectedResult, result)
        verify(tripRepository).insert(tripName)
        verify(companionRepository).insert(tripId.toInt(), *companions.toTypedArray())
    }
}