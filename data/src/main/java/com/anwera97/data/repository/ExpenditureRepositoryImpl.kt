package com.anwera97.data.repository

import androidx.annotation.WorkerThread
import com.anwera97.data.composedclasses.ExpenditureWithDebtorsAndPayer
import com.anwera97.data.dao.DebtorsDao
import com.anwera97.data.dao.ExpenditureDao
import com.anwera97.data.entities.Debtors
import com.anwera97.data.mappers.ExpenseMapper
import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.domain.models.ExpenseCreationData
import com.anwera97.domain.repositories.ExpenditureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenditureRepositoryImpl(
    private val expenditureDao: ExpenditureDao,
    private val debtorsDao: DebtorsDao
) : ExpenditureRepository {

    override fun getExpense(tripId: Int): Flow<List<ExpenditureModel>> {
        return expenditureDao.getAllFromTrip(tripId).map(::mapToExpenditureModels)
    }

    @WorkerThread
    override suspend fun addExpense(expense: ExpenseCreationData, debtorIds: Map<Int, Double>) {
        val entity = ExpenseMapper.toEntity(expense)
        val id = expenditureDao.insert(entity)
        debtorIds.map { entry -> createDebtors(entry, id) }.forEach(debtorsDao::insert)
    }

    private fun createDebtors(entry: Map.Entry<Int, Double>, id: Long): Debtors {
        return Debtors(entry.key, id.toInt(), entry.value)
    }

    private fun mapToExpenditureModels(list: List<ExpenditureWithDebtorsAndPayer>): List<ExpenditureModel> {
        return list.map(ExpenseMapper::toModel)
    }
}