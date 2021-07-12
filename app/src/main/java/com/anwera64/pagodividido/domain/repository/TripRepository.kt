package com.anwera64.pagodividido.domain.repository

import androidx.annotation.WorkerThread
import com.anwera64.pagodividido.data.dao.TripDao
import com.anwera64.pagodividido.data.entities.Trip
import com.anwera64.pagodividido.domain.mappers.TripMapper.toModel
import com.anwera64.pagodividido.domain.models.TripModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripRepository(private val tripDao: TripDao) {

    val allTrips: Flow<List<TripModel>> = tripDao.getAll().map { list -> list.map(::toModel) }

    @WorkerThread
    suspend fun insert(trip: Trip): Long {
        return tripDao.insertAll(trip)
    }

    fun getTripById(id: Int) = tripDao.getTripById(id)
}