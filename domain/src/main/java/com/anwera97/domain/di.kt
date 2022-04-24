package com.anwera97.domain

import com.anwera97.data.dataModule
import com.anwera97.domain.usecases.*
import org.koin.dsl.module

val domainModule = module {
    factory { CreateTripUseCase(get(), get()) }
    factory { ExpenditureUseCase(get()) }
    factory { TripsUseCase(get()) }
    factory { NewExpenditureUseCase(get(), get()) }
    factory { CompanionResultUseCase(get()) }
}

val domainAndDataModules = listOf(dataModule, domainModule)
