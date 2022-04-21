package com.anwera64.pagodividido.trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.domain.usecases.ExpenditureUseCase

class TripViewModel(private val expenditureUseCase: ExpenditureUseCase) : ViewModel() {

    fun getTripDetails(tripUid: Int): LiveData<List<ExpenditureModel>> {
        return expenditureUseCase.getExpenditures(tripUid).asLiveData()
    }

}