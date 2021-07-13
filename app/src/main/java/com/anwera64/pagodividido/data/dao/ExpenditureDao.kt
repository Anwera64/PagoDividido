package com.anwera64.pagodividido.data.dao

import androidx.room.*
import com.anwera64.pagodividido.data.composedclasses.ExpenditureWithDebtors
import com.anwera64.pagodividido.data.entities.Expenditure
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenditureDao {
    @Query("SELECT * FROM expenditure WHERE trip_id = :tripId")
    fun getAllFromTrip(tripId: Int): Flow<List<ExpenditureWithDebtors>>

    @Insert
    suspend fun insert(expenditure: Expenditure): Long
}