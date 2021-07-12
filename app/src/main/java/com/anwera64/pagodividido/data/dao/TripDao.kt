package com.anwera64.pagodividido.data.dao

import androidx.room.*
import com.anwera64.pagodividido.data.composedclasses.TripWithCompanions
import com.anwera64.pagodividido.data.entities.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trip")
    fun getAll(): Flow<List<TripWithCompanions>>

    @Query("SELECT * FROM trip WHERE id == :id")
    fun getTripById(id: Int): Flow<Trip>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(trips: Trip): Long

    @Query("DELETE FROM trip where id == :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM trip")
    suspend fun deleteAll()
}