package com.anwera97.domain

import com.anwera97.data.repository.CompanionRepository
import com.anwera97.data.repository.ExpenditureRepository
import com.anwera97.data.repository.TripRepository
import com.anwera97.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object DomainModule {

    @Provides
    fun providesCreateTripUseCase(tripRepository: TripRepository, companionRepository: CompanionRepository) : CreateTripUseCase {
        return CreateTripUseCase(tripRepository, companionRepository)
    }

    @Provides
    fun providesExpenditureUseCase(expenditureRepository: ExpenditureRepository) : ExpenditureUseCase {
        return ExpenditureUseCase(expenditureRepository)
    }

    @Provides
    fun providesTripUseCase(tripRepository: TripRepository) : TripsUseCase {
        return TripsUseCase(tripRepository)
    }

    @Provides
    fun providesNewExpenditureUseCase(companionRepository: CompanionRepository, expenditureRepository: ExpenditureRepository) : NewExpenditureUseCase {
        return NewExpenditureUseCase(companionRepository, expenditureRepository)
    }

    @Provides
    fun providesCompanionResultUseCase(companionRepository: CompanionRepository) : CompanionResultUseCase {
        return CompanionResultUseCase(companionRepository)
    }

}