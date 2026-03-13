package com.anwera97.data.dao

import androidx.room.*
import com.anwera97.data.composedclasses.PayerWithExpendituresAndDebtors
import com.anwera97.data.entities.CompanionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanionDao {
    @Query("SELECT * FROM companion WHERE trip_id = :tripId")
    fun getAllFromTrip(tripId: Int): Flow<List<CompanionEntity>>

    @Transaction
    @Query("SELECT * FROM companion WHERE trip_id = :tripId")
    fun getPayersWithDebtors(tripId: Int): Flow<List<PayerWithExpendituresAndDebtors>>

    @Insert
    suspend fun insertAll(vararg companion: CompanionEntity)

    @Delete
    suspend fun delete(companion: CompanionEntity)
}