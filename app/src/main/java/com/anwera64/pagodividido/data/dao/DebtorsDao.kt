package com.anwera64.pagodividido.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.anwera64.pagodividido.data.entities.Debtors

@Dao
interface DebtorsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(join: Debtors)
}