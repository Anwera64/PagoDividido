package com.anwera97.domain.usecases

import com.anwera97.domain.models.TripModel
import com.anwera97.domain.repositories.TripRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TripsUseCase @Inject constructor(private val tripRepository: TripRepository) {

    fun getAllTrips(): Flow<List<TripModel>> = tripRepository.getAllTrips()

    suspend fun deleteTrip(id: Int) {
        tripRepository.deleteTrip(id)
    }
}