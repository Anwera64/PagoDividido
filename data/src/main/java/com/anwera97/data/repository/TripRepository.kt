package com.anwera97.data.repository

import androidx.annotation.WorkerThread

class TripRepository(private val tripDao: com.anwera97.data.dao.TripDao) {

    fun getAllTrips() = tripDao.getAll()

    @WorkerThread
    suspend fun insert(trip: com.anwera97.data.entities.Trip): Long {
        return tripDao.insertAll(trip)
    }

    fun getTripById(id: Int) = tripDao.getTripById(id)

    @WorkerThread
    suspend fun deleteTrip(tripId: Int) = tripDao.deleteById(tripId)
}