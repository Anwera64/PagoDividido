package com.anwera64.pagodividido.newexpenditure

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.usecases.NewExpenditureUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class NewExpenditureViewModel(private val useCase: NewExpenditureUseCase) : ViewModel() {

    private val _companions = MediatorLiveData<List<CompanionModel>>()
    var companions: LiveData<List<CompanionModel>> = _companions

    fun createExpenditure(
        tripId: Int,
        payerId: Int,
        debtors: HashMap<Int, Double>,
        detail: String?,
        amountSpent: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.addExpenditure(tripId, payerId, debtors, detail, amountSpent)
        }
    }

    fun getCompanions(tripId: Int) {
        _companions.addSource(useCase.getTripCompanions(tripId).asLiveData(), Observer(_companions::postValue))
    }
}