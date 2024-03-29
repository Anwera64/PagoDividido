package com.anwera97.domain.usecases

import com.anwera97.domain.models.TripShortModel
import com.anwera97.domain.repositories.CompanionRepository
import com.anwera97.domain.repositories.TripRepository
import javax.inject.Inject

class CreateTripUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    private val companionRepository: CompanionRepository
) {

    fun checkCompanionsSize(list: List<String>): Boolean {
        return list.isNotEmpty() && list.size >= 2
    }

    fun checkTripName(name: String): Boolean {
        return name.isNotEmpty() && name.isNotBlank()
    }

    suspend fun createTrip(companions: List<String>, tripName: String): TripShortModel {
        val tripId = tripRepository.insert(tripName).toInt()
        companionRepository.insert(tripId, *companions.toTypedArray())
        return TripShortModel(tripName, tripId)
    }
}