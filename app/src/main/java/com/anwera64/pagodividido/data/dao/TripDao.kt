package com.anwera64.pagodividido.data.dao

import androidx.room.*
import com.anwera64.pagodividido.data.entities.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trip")
    fun getAll(): Flow<List<Trip>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg trips: Trip)

    @Delete
    suspend fun delete(trip: Trip)

    @Query("DELETE FROM trip")
    suspend fun deleteAll()
}