package com.anwera64.infra

import com.anwera97.data.dao.CompanionDao
import com.anwera97.data.dao.DebtorsDao
import com.anwera97.data.dao.ExpenditureDao
import com.anwera97.data.dao.TripDao
import com.anwera97.data.repository.CompanionRepositoryImpl
import com.anwera97.data.repository.ExpenditureRepositoryImpl
import com.anwera97.data.repository.TripRepositoryImpl
import com.anwera97.domain.repositories.CompanionRepository
import com.anwera97.domain.repositories.ExpenditureRepository
import com.anwera97.domain.repositories.TripRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providesTripRepository(tripDao: TripDao): TripRepository = TripRepositoryImpl(tripDao)

    @Provides
    fun providesCompanionRepository(companionDao: CompanionDao): CompanionRepository =
        CompanionRepositoryImpl(companionDao)

    @Provides
    fun providesExpenditureRepository(
        expenditureDao: ExpenditureDao,
        debtorsDao: DebtorsDao
    ): ExpenditureRepository {
        return ExpenditureRepositoryImpl(expenditureDao, debtorsDao)
    }
}