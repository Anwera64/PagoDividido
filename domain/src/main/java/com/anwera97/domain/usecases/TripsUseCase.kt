package com.anwera97.domain.usecases

import com.anwera97.data.repository.TripRepository
import com.anwera97.domain.mappers.TripMapper
import com.anwera97.domain.models.TripModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripsUseCase(private val tripRepository: TripRepository) {

    fun getAllTrips(): Flow<List<TripModel>> =
        tripRepository.getAllTrips().map { list -> list.map(TripMapper::toModel) }

    suspend fun deleteTrip(id: Int) {
        tripRepository.deleteTrip(id)
    }
}