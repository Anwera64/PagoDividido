package com.anwera97.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.anwera97.data.entities.Debtors

@Dao
interface DebtorsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(join: Debtors)
}