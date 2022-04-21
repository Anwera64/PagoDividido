package com.anwera97.data.repository

import androidx.annotation.WorkerThread
import com.anwera97.data.dao.CompanionDao
import com.anwera97.data.entities.Companion

class CompanionRepository(private val companionDao: CompanionDao) {

    fun getTripCompanions(tripId: Int) = companionDao.getAllFromTrip(tripId)

    fun getPayersWithDebtors(tripId: Int) = companionDao.getPayersWithDebtors(tripId)

    @WorkerThread
    suspend fun insert(companion: Companion) {
        companionDao.insertAll(companion)
    }
}