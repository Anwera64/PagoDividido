package com.anwera97.data.repository

import androidx.annotation.WorkerThread
import com.anwera97.data.composedclasses.PayerWithExpendituresAndDebtors
import com.anwera97.data.dao.CompanionDao
import com.anwera97.data.entities.Companion
import com.anwera97.data.mappers.CompanionMapper
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.ResultModel
import com.anwera97.domain.repositories.CompanionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CompanionRepositoryImpl(private val companionDao: CompanionDao) : CompanionRepository {

    override fun getTripCompanions(tripId: Int): Flow<List<CompanionModel>> =
        companionDao.getAllFromTrip(tripId).map(::mapToCompanionList)

    override fun getResultInfoFor(tripId: Int): Flow<List<ResultModel>> {
        return companionDao.getPayersWithDebtors(tripId)
            .map { listOfEntities: List<PayerWithExpendituresAndDebtors> ->
                listOfEntities.map(CompanionMapper::toModel)
            }
    }

    @WorkerThread
    override suspend fun insert(tripId: Int, vararg companionName: String) {
        val companionEntities = companionName.map { name -> Companion(name, tripId) }
        companionDao.insertAll(*companionEntities.toTypedArray())
    }

    private fun mapToCompanionList(list: List<Companion>): List<CompanionModel> {
        return list.map(CompanionMapper::toModel)
    }
}