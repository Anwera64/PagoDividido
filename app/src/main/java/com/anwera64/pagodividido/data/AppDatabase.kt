package com.anwera64.pagodividido.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anwera64.pagodividido.data.dao.CompanionDao
import com.anwera64.pagodividido.data.dao.TripDao
import com.anwera64.pagodividido.data.entities.Companion
import com.anwera64.pagodividido.data.entities.Debtors
import com.anwera64.pagodividido.data.entities.Expenditure
import com.anwera64.pagodividido.data.entities.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Trip::class, Companion::class, Expenditure::class, Debtors::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
    abstract fun companionDao(): CompanionDao

    //Just for population. Todo() remove this
    private class TripDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.tripDao())
                }
            }
        }

        suspend fun populateDatabase(tripDao: TripDao) {
            // Delete all content here.
            tripDao.deleteAll()
            // Add sample words.
            tripDao.insertAll(Trip("Miami 1"), Trip("Miami 2"))
        }
    }

    //TODO Temporal. Migrate to Dagger next
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "app_database"

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(TripDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}