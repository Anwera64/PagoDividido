package com.anwera64.pagodividido.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anwera64.pagodividido.data.composedclasses.PayerWithExpendituresAndDebtors
import com.anwera64.pagodividido.data.entities.Companion
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanionDao {
    @Query("SELECT * FROM companion WHERE trip_id = :tripId")
    fun getAllFromTrip(tripId: Int): Flow<List<Companion>>

    @Transaction
    @Query("SELECT * FROM companion WHERE trip_id = :tripId")
    fun getPayersWithDebtors(tripId: Int): Flow<List<PayerWithExpendituresAndDebtors>>

    @Insert
    suspend fun insertAll(vararg companion: Companion)

    @Delete
    suspend fun delete(companion: Companion)
}