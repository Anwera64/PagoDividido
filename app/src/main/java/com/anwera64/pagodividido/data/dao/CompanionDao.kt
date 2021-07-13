package com.anwera64.pagodividido.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.anwera64.pagodividido.data.entities.Companion
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanionDao {
    @Query("SELECT * FROM companion WHERE trip_id = :tripId")
    fun getAllFromTrip(tripId: Int): Flow<List<Companion>>

    @Insert
    suspend fun insertAll(vararg companion: Companion)

    @Delete
    suspend fun delete(companion: Companion)
}