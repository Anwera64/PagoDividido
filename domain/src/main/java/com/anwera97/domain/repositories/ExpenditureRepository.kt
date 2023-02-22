package com.anwera97.domain.repositories

import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.domain.models.ExpenseCreationData
import kotlinx.coroutines.flow.Flow


interface ExpenditureRepository {

    fun getExpense(tripId: Int): Flow<List<ExpenditureModel>>

    suspend fun addExpense(expense: ExpenseCreationData, debtorIds: Map<Int, Double>)
}