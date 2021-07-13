package com.anwera64.pagodividido.presentation

import android.app.Application
import com.anwera64.pagodividido.data.AppDatabase
import com.anwera64.pagodividido.domain.repository.CompanionRepository
import com.anwera64.pagodividido.domain.repository.ExpenditureRepository
import com.anwera64.pagodividido.domain.repository.TripRepository
import com.anwera64.pagodividido.presentation.main.MainActivity
import com.anwera64.pagodividido.presentation.main.MainViewModel
import com.anwera64.pagodividido.presentation.newtrip.NewTripActivity
import com.anwera64.pagodividido.presentation.newtrip.NewTripViewModel
import com.anwera64.pagodividido.presentation.trip.TripViewModel
import com.anwera64.pagodividido.presentation.trip.detail.TripDetailFragment
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(serviceModule, dataModule, scopeModule))
    }

}

//TODO: separate injection file into different layers when we implement clean architecture

private val serviceModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
    single { provideTripDao(get()) }
    single { provideCompanionDao(get()) }
    single { provideExpenditureDao(get()) }
}

private fun provideTripDao(database: AppDatabase) = database.tripDao()
private fun provideCompanionDao(database: AppDatabase) = database.companionDao()
private fun provideExpenditureDao(database: AppDatabase) = database.expenditureDao()

private val dataModule = module {
    single { TripRepository(get()) }
    single { CompanionRepository(get()) }
    single { ExpenditureRepository(get()) }
}

private val scopeModule = module {
    scope(named<MainActivity>()) {
        viewModel { MainViewModel(get()) }
    }
    scope(named<NewTripActivity>()) {
        viewModel { NewTripViewModel(get(), get()) }
    }
    scope(named<TripDetailFragment>()) {
        viewModel { TripViewModel(get(), get(), get()) }
    }
}