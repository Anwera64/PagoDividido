package com.anwera97.data.repository

import androidx.annotation.WorkerThread
import com.anwera97.data.dao.DebtorsDao
import com.anwera97.data.dao.ExpenditureDao
import com.anwera97.data.entities.Debtors
import com.anwera97.data.entities.Expenditure

class ExpenditureRepository(
    private val expenditureDao: ExpenditureDao,
    private val debtorsDao: DebtorsDao
) {

    fun getExpenditures(tripId: Int) = expenditureDao.getAllFromTrip(tripId)

    @WorkerThread
    suspend fun addExpenditure(expenditure: Expenditure, debtorIds: HashMap<Int, Double>) {
        val id = expenditureDao.insert(expenditure)
        debtorIds.map { entry -> createDebtors(entry, id) }.forEach(debtorsDao::insert)
    }

    private fun createDebtors(entry: Map.Entry<Int, Double>, id: Long): Debtors {
        return Debtors(entry.key, id.toInt(), entry.value)
    }
}