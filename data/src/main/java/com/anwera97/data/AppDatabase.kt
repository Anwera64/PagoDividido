package com.anwera97.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anwera97.data.dao.CompanionDao
import com.anwera97.data.dao.DebtorsDao
import com.anwera97.data.dao.ExpenditureDao
import com.anwera97.data.dao.TripDao
import com.anwera97.data.entities.Companion
import com.anwera97.data.entities.Debtors
import com.anwera97.data.entities.Expenditure
import com.anwera97.data.entities.Trip

@Database(
        entities = [Trip::class, Companion::class, Expenditure::class, Debtors::class],
        version = 1,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
    abstract fun companionDao(): CompanionDao
    abstract fun expenditureDao(): ExpenditureDao
    abstract fun debtorsDao(): DebtorsDao

    //TODO Temporal. Migrate to Dagger next
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "app_database"

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                )
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}