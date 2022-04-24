package com.anwera64.pagodividido

import android.app.Application
import com.anwera64.pagodividido.main.MainActivity
import com.anwera64.pagodividido.main.MainViewModel
import com.anwera64.pagodividido.newexpenditure.NewExpenditureActivity
import com.anwera64.pagodividido.newexpenditure.NewExpenditureViewModel
import com.anwera64.pagodividido.newtrip.NewTripActivity
import com.anwera64.pagodividido.newtrip.NewTripViewModel
import com.anwera64.pagodividido.trip.TripViewModel
import com.anwera64.pagodividido.trip.companionresult.CompanionResultFragment
import com.anwera64.pagodividido.trip.companionresult.CompanionResultViewModel
import com.anwera64.pagodividido.trip.detail.TripDetailFragment
import com.anwera97.domain.domainAndDataModules
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
        modules(domainAndDataModules.plus(scopeModule))
    }
}

private val scopeModule = module {
    scope(named<MainActivity>()) {
        viewModel { MainViewModel(get()) }
    }
    scope(named<NewTripActivity>()) {
        viewModel { NewTripViewModel(get()) }
    }
    scope(named<TripDetailFragment>()) {
        viewModel { TripViewModel(get()) }
    }
    scope(named<NewExpenditureActivity>()) {
        viewModel { NewExpenditureViewModel(get()) }
    }
    scope(named<CompanionResultFragment>()) {
        viewModel { CompanionResultViewModel(get()) }
    }
}