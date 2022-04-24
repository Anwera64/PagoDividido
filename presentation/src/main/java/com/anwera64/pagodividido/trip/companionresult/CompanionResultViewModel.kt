package com.anwera64.pagodividido.trip.companionresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.anwera97.domain.models.ResultModel
import com.anwera97.domain.usecases.CompanionResultUseCase

class CompanionResultViewModel(private val useCase: CompanionResultUseCase) : ViewModel() {

    fun getItems(tripId: Int): LiveData<List<ResultModel>> {
        return useCase.getPayersWithDebtors(tripId).asLiveData()
    }
}