package com.anwera64.pagodividido.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.anwera64.pagodividido.data.composedclasses.ExpenditureDetailEntity
import com.anwera64.pagodividido.data.composedclasses.ExpenditureWithDebtorsAndPayer
import com.anwera64.pagodividido.data.entities.Expenditure
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenditureDao {
    @Query("SELECT * FROM expenditure WHERE trip_id = :tripId")
    fun getAllFromTrip(tripId: Int): Flow<List<ExpenditureWithDebtorsAndPayer>>

    @Query("SELECT * FROM expenditure where id = :id LIMIT 1")
    fun getDetail(id: Int): Flow<ExpenditureDetailEntity>

    @Insert
    suspend fun insert(expenditure: Expenditure): Long
}