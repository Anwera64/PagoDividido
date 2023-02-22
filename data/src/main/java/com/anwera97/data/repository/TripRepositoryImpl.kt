package com.anwera97.data.repository

import androidx.annotation.WorkerThread
import com.anwera97.data.dao.TripDao
import com.anwera97.data.entities.Trip
import com.anwera97.data.mappers.TripMapper
import com.anwera97.domain.models.TripModel
import com.anwera97.domain.repositories.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripRepositoryImpl(private val tripDao: TripDao) : TripRepository {

    override fun getAllTrips(): Flow<List<TripModel>> {
        return tripDao.getAll().map { list -> list.map(TripMapper::toModel) }
    }

    @WorkerThread
    override suspend fun insert(tripName: String): Long {
        return tripDao.insertAll(Trip(tripName))
    }

    @WorkerThread
    override suspend fun deleteTrip(id: Int) = tripDao.deleteById(id)
}