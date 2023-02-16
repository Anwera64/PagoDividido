package com.anwera97.domain.repositories

import com.anwera97.domain.models.TripModel
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    fun getAllTrips(): Flow<List<TripModel>>

    suspend fun deleteTrip(id: Int)

    suspend fun insert(tripName: String): Long
}