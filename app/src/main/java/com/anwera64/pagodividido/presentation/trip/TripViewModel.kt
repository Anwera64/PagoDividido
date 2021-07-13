package com.anwera64.pagodividido.presentation.trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.anwera64.pagodividido.domain.models.ExpenditureModel
import com.anwera64.pagodividido.domain.repository.CompanionRepository
import com.anwera64.pagodividido.domain.repository.ExpenditureRepository
import com.anwera64.pagodividido.domain.repository.TripRepository

class TripViewModel(
    private val companionRepository: CompanionRepository,
    private val tripRepository: TripRepository,
    private val expenditureRepository: ExpenditureRepository
) : ViewModel() {

    fun getTripDetails(tripUid: Int): LiveData<List<ExpenditureModel>> {
       return expenditureRepository.getExpenditures(tripUid).asLiveData()
    }

}