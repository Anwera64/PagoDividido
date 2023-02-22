package com.anwera97.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anwera97.data.composedclasses.TripWithCompanions
import com.anwera97.data.entities.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trip")
    fun getAll(): Flow<List<TripWithCompanions>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(trips: Trip): Long

    @Query("DELETE FROM trip where id == :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM trip")
    suspend fun deleteAll()
}