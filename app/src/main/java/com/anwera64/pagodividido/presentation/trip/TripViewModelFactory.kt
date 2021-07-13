package com.anwera64.pagodividido.presentation.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anwera64.pagodividido.domain.repository.CompanionRepository
import com.anwera64.pagodividido.domain.repository.ExpenditureRepository
import com.anwera64.pagodividido.domain.repository.TripRepository

class TripViewModelFactory(
    private val tripRepository: TripRepository,
    private val companionRepository: CompanionRepository,
    private val expenditureRepository: ExpenditureRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            return TripViewModel(
                tripRepository = tripRepository,
                companionRepository = companionRepository,
                expenditureRepository = expenditureRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}