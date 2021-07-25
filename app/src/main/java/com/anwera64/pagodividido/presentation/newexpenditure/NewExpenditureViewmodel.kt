package com.anwera64.pagodividido.presentation.newexpenditure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anwera64.pagodividido.data.entities.Expenditure
import com.anwera64.pagodividido.domain.models.CompanionModel
import com.anwera64.pagodividido.domain.repository.CompanionRepository
import com.anwera64.pagodividido.domain.repository.ExpenditureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class NewExpenditureViewModel(
        private val companionRepository: CompanionRepository,
        private val expenditureRepository: ExpenditureRepository
) : ViewModel() {

    private val _companions = MutableLiveData<List<CompanionModel>>()
    var companions: LiveData<List<CompanionModel>> = _companions

    fun createExpenditure(
            tripId: Int,
            payerId: Int,
            debtors: HashMap<Int, Double>,
            detail: String?,
            amountSpent: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            expenditureRepository.addExpenditure(
                    Expenditure(
                            expense = amountSpent,
                            imageRef = null,
                            date = Date(),
                            tripId = tripId,
                            payerId = payerId,
                            detail = if (detail.isNullOrEmpty()) null else detail
                    ),
                    debtorIds = debtors
            )
        }
    }

    fun getCompanions(tripId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            companionRepository.getTripCompanions(tripId).collect(_companions::postValue)
        }
    }
}