package com.anwera97.domain

import com.anwera97.domain.models.TripModel
import com.anwera97.domain.repositories.TripRepository
import com.anwera97.domain.usecases.TripsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TripsUseCaseTest {

    private val tripRepository: TripRepository = mock()
    private val tripsUseCase = TripsUseCase(tripRepository)

    @Test
    fun `get all trips delegates to repository`() = runTest {
        val expectedTrips = listOf(
            TripModel(uid = "1", totalSpent = 10.0, name = "Beach", companions = emptyList())
        )
        whenever(tripRepository.getAllTrips()) doReturn flowOf(expectedTrips)

        val result = tripsUseCase.getAllTrips().first()

        assertEquals(1, result.size)
        assertEquals("1", result[0].uid)
        assertEquals(10.0, result[0].totalSpent, 0.0)
        assertEquals("Beach", result[0].name)
    }

    @Test
    fun `delete trip delegates to repository`() = runTest {
        tripsUseCase.deleteTrip(42)

        verify(tripRepository).deleteTrip(42)
    }
}

