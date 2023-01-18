package com.anwera64.pagodividido.newexpenditure

import androidx.lifecycle.*
import com.anwera64.pagodividido.utils.EventWrapper
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.DebtorInputError
import com.anwera97.domain.models.InputErrorTypes
import com.anwera97.domain.usecases.CompanionsUseCase
import com.anwera97.domain.usecases.NewExpenditureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewExpenditureViewModel @Inject constructor(
    private val newExpenditureUseCase: NewExpenditureUseCase,
    private val companionsUseCase: CompanionsUseCase,
) : ViewModel() {

    private val _companions = MediatorLiveData<List<CompanionModel>>()
    var companions: LiveData<List<CompanionModel>> = _companions

    private val _debtInputErrors = MutableLiveData<List<DebtorInputError>>()
    val debtInputErrors: LiveData<List<DebtorInputError>> = _debtInputErrors

    private val _showMatchErrorDialog = MutableLiveData<EventWrapper<Double>>()
    val showMatchErrorDialog: LiveData<EventWrapper<Double>> = _showMatchErrorDialog

    private val _amountInputError = MutableLiveData<InputErrorTypes>()
    val amountInputError: LiveData<InputErrorTypes> = _amountInputError

    private val _payerInputError = MutableLiveData<InputErrorTypes>()
    val payerInputError: LiveData<InputErrorTypes> = _payerInputError

    private val _insertionDone = MutableLiveData<EventWrapper<Boolean>>()
    val insertonDone: LiveData<EventWrapper<Boolean>> = _insertionDone

    fun createExpenditure(
        tripId: Int,
        payerId: Int,
        debtors: Map<Int, Double>,
        detail: String?,
        amountString: String
    ) {
        val payerIdError = newExpenditureUseCase.checkPayerInputError(payerId)
        _payerInputError.postValue(payerIdError)
        val amountInputError = newExpenditureUseCase.checkAmountInputError(amountString)
        _amountInputError.postValue(amountInputError)
        if (amountInputError != InputErrorTypes.NO_ERROR || payerIdError != InputErrorTypes.NO_ERROR) {
            return
        }
        val amountSpent = amountString.toDouble()

        val errors: List<DebtorInputError> = newExpenditureUseCase.lookForDebtorInputErrors(amountSpent, debtors)
        if (errors.isNotEmpty()) {
            _debtInputErrors.postValue(errors)
            return
        }

        val amountsDifference = newExpenditureUseCase.checkAmountsDifference(amountSpent, debtors)
        val amountsMismatch = amountsDifference != 0.0
        if (amountsMismatch) {
            _showMatchErrorDialog.postValue(EventWrapper(amountsDifference))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            newExpenditureUseCase.addExpenditure(tripId, payerId, debtors, detail, amountSpent)
            _insertionDone.postValue(EventWrapper(true))
        }
    }

    fun getCompanions(tripId: Int) {
        _companions.addSource(
            companionsUseCase.getTripCompanions(tripId).asLiveData(),
            Observer(_companions::postValue)
        )
    }
}