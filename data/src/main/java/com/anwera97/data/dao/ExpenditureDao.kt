package com.anwera97.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.anwera97.data.composedclasses.ExpenditureWithDebtorsAndPayer
import com.anwera97.data.composedclasses.ExpenditureWithPayer
import com.anwera97.data.entities.Expenditure
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenditureDao {
    @Query("SELECT * FROM expenditure WHERE trip_id = :tripId")
    fun getAllFromTrip(tripId: Int): Flow<List<ExpenditureWithPayer>>

    @Query("SELECT * FROM expenditure WHERE id = :id")
    fun getExpenditure(id: Int): Flow<ExpenditureWithDebtorsAndPayer>

    @Insert
    suspend fun insert(expenditure: Expenditure): Long
}