package com.anwera64.pagodividido.presentation.newtrip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anwera64.pagodividido.domain.repository.CompanionRepository
import com.anwera64.pagodividido.domain.repository.TripRepository

class NewTripViewModelFactory(
    private val tripRepository: TripRepository,
    private val companionRepository: CompanionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewTripViewModel::class.java)) {
            return NewTripViewModel(tripRepository, companionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}