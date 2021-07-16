package com.anwera64.pagodividido.presentation.trip.companionresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.anwera64.pagodividido.domain.repository.CompanionRepository

class CompanionResultViewModel(private val companionRepository: CompanionRepository) : ViewModel() {

    fun getItems(tripId: Int) = companionRepository.getPayersWithDebtors(tripId).asLiveData()
}