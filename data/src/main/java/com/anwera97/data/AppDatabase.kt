package com.anwera97.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anwera97.data.dao.CompanionDao
import com.anwera97.data.dao.DebtorsDao
import com.anwera97.data.dao.ExpenditureDao
import com.anwera97.data.dao.TripDao
import com.anwera97.data.entities.CompanionEntity
import com.anwera97.data.entities.Debtors
import com.anwera97.data.entities.Expenditure
import com.anwera97.data.entities.Trip

@Database(
        entities = [Trip::class, CompanionEntity::class, Expenditure::class, Debtors::class],
        version = 1,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
    abstract fun companionDao(): CompanionDao
    abstract fun expenditureDao(): ExpenditureDao
    abstract fun debtorsDao(): DebtorsDao
}