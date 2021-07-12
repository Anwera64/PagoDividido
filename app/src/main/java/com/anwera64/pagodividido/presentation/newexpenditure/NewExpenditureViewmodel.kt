package com.anwera64.pagodividido.presentation.newexpenditure

import androidx.lifecycle.*
import androidx.lifecycle.Observer
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
        payerName: String,
        debtors: ArrayList<Int>,
        detail: String?,
        amountSpent: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val payer = companions.value?.find { companionModel ->
                companionModel.name.equals(payerName, ignoreCase = true)
            } ?: return@launch
            expenditureRepository.addExpenditure(
                Expenditure(
                    expense = amountSpent,
                    imageRef = null,
                    date = Date(),
                    tripId = tripId,
                    payerId = payer.uid.toInt(),
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