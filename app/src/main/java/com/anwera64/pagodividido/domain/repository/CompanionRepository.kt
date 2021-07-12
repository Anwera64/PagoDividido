package com.anwera64.pagodividido.domain.repository

import androidx.annotation.WorkerThread
import com.anwera64.pagodividido.data.AppDatabase
import com.anwera64.pagodividido.data.dao.CompanionDao
import com.anwera64.pagodividido.data.entities.Companion
import com.anwera64.pagodividido.domain.mappers.CompanionMapper
import com.anwera64.pagodividido.domain.models.CompanionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CompanionRepository(private val companionDao: CompanionDao) {

    fun getTripCompanions(tripId: Int): Flow<List<CompanionModel>> {
        return companionDao
            .getAllFromTrip(tripId)
            .map { list -> list.map(CompanionMapper::toModel) }
    }

    @WorkerThread
    suspend fun insert(companion: Companion) {
        companionDao.insertAll(companion)
    }
}