package com.anwera97.data

import com.anwera97.data.repository.CompanionRepository
import com.anwera97.data.repository.ExpenditureRepository
import com.anwera97.data.repository.TripRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
    single { provideTripDao(get()) }
    single { provideCompanionDao(get()) }
    single { provideExpenditureDao(get()) }
    single { provideDebtsDao(get()) }
    single { TripRepository(get()) }
    single { CompanionRepository(get()) }
    single { ExpenditureRepository(get(), get()) }
}

private fun provideTripDao(database: AppDatabase) = database.tripDao()
private fun provideCompanionDao(database: AppDatabase) = database.companionDao()
private fun provideExpenditureDao(database: AppDatabase) = database.expenditureDao()
private fun provideDebtsDao(database: AppDatabase) = database.debtorsDao()
