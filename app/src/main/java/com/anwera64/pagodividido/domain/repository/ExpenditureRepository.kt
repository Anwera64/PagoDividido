package com.anwera64.pagodividido.domain.repository

import androidx.annotation.WorkerThread
import com.anwera64.pagodividido.data.dao.ExpenditureDao
import com.anwera64.pagodividido.data.entities.Expenditure
import com.anwera64.pagodividido.domain.mappers.ExpenditureMapper
import com.anwera64.pagodividido.domain.models.ExpenditureModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenditureRepository(private val expenditureDao: ExpenditureDao) {

    fun getExpenditures(tripId: Int): Flow<List<ExpenditureModel>> {
        return expenditureDao
            .getAllFromTrip(tripId)
            .map { list -> list.map(ExpenditureMapper::toModel) }
    }

    @WorkerThread
    suspend fun addExpenditure(expenditure: Expenditure) {
        expenditureDao.insert(expenditure)
    }
}