package com.anwera97.domain.usecases

import com.anwera97.data.entities.Companion
import com.anwera97.data.entities.Trip
import com.anwera97.data.repository.CompanionRepository
import com.anwera97.data.repository.TripRepository
import com.anwera97.domain.models.TripShortModel

class CreateTripUseCase(
    private val tripRepository: TripRepository,
    private val companionRepository: CompanionRepository
) {

    fun checkCompanionsSize(list: List<String>): Boolean {
        return !(list.isEmpty() || list.size < 2)
    }

    fun checkTripName(name: String): Boolean {
        return !(name.isEmpty() || name.isBlank())
    }

    suspend fun createTrip(companions: List<String>, tripName: String): TripShortModel {
        val tripId = tripRepository.insert(Trip(tripName)).toInt()
        companions.forEach { name ->
            companionRepository.insert(Companion(name, tripId))
        }
        return TripShortModel(tripName, tripId)
    }
}