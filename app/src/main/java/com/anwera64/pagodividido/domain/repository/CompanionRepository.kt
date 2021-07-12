package com.anwera64.pagodividido.domain.repository

import androidx.annotation.WorkerThread
import com.anwera64.pagodividido.data.AppDatabase
import com.anwera64.pagodividido.data.dao.CompanionDao
import com.anwera64.pagodividido.data.entities.Companion

class CompanionRepository(private val companionDao: CompanionDao) {

    @WorkerThread
    suspend fun insert(companion: Companion) {
        companionDao.insertAll(companion)
    }
}