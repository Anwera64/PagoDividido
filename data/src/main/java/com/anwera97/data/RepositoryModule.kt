package com.anwera97.data

import com.anwera97.data.repository.CompanionRepositoryImpl
import com.anwera97.data.repository.ExpenditureRepositoryImpl
import com.anwera97.data.repository.TripRepositoryImpl
import com.anwera97.domain.repositories.CompanionRepository
import com.anwera97.domain.repositories.ExpenditureRepository
import com.anwera97.domain.repositories.TripRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTripRepository(impl: TripRepositoryImpl): TripRepository

    @Binds
    abstract fun bindCompanionRepository(impl: CompanionRepositoryImpl): CompanionRepository

    @Binds
    abstract fun bindExpenditureRepository(impl: ExpenditureRepositoryImpl): ExpenditureRepository
}
