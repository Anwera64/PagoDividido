package com.anwera64.pagodividido.trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.domain.models.ResultModel
import com.anwera97.domain.usecases.CompanionResultUseCase
import com.anwera97.domain.usecases.ExpenditureUseCase

class TripViewModel(
    private val expenditureUseCase: ExpenditureUseCase,
    private val useCase: CompanionResultUseCase
) : ViewModel() {

    fun getExpenseList(tripUid: Int): LiveData<List<ExpenditureModel>> {
        return expenditureUseCase.getExpenditures(tripUid).asLiveData()
    }

    fun getResults(tripId: Int): LiveData<List<ResultModel>> {
        return useCase.getPayersWithDebtors(tripId).asLiveData()
    }
}