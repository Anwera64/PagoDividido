package com.anwera97.data

import android.content.Context
import androidx.room.Room
import com.anwera97.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }

    @Singleton
    @Provides
    fun providesTripDao(database: AppDatabase): TripDao = database.tripDao()

    @Singleton
    @Provides
    fun providesCompanionDao(database: AppDatabase): CompanionDao = database.companionDao()

    @Singleton
    @Provides
    fun providesExpenditureDao(database: AppDatabase): ExpenditureDao = database.expenditureDao()

    @Singleton
    @Provides
    fun providesDebtorsDao(database: AppDatabase): DebtorsDao = database.debtorsDao()

    @Singleton
    @Provides
    fun providesResultDao(database: AppDatabase): ResultDao = database.resultDao()
}