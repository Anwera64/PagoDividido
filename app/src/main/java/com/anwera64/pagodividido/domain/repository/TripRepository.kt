package com.anwera64.pagodividido.domain.repository

import androidx.annotation.WorkerThread
import com.anwera64.pagodividido.data.dao.TripDao
import com.anwera64.pagodividido.data.entities.Trip
import kotlinx.coroutines.flow.Flow

class TripRepository(private val tripDao: TripDao) {

    val allTrips: Flow<List<Trip>> = tripDao.getAll()

    @WorkerThread
    suspend fun insert(trip: Trip) {
        tripDao.insertAll(trip)
    }
}