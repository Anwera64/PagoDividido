package com.anwera64.pagodividido.newexpenditure

import androidx.lifecycle.*
import com.anwera64.pagodividido.newexpenditure.utils.NewExpenseErrorModel
import com.anwera64.pagodividido.newexpenditure.utils.NewExpenseErrorStates
import com.anwera64.pagodividido.newexpenditure.utils.NewExpenseErrorStates.Companion.toErrorState
import com.anwera64.pagodividido.newexpenditure.utils.PaymentOptions
import com.anwera64.pagodividido.utils.EventWrapper
import com.anwera64.pagodividido.utils.modifyLiveDataMap
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.DebtorInputError
import com.anwera97.domain.models.InputErrorType
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

    // Data
    private val _companions = MediatorLiveData<List<CompanionModel>>()
    var companions: LiveData<List<CompanionModel>> = _companions

    private val paymentRelationship = MutableLiveData<Map<Int, Double>>(emptyMap())

    private val _payerId = MutableLiveData<Int>()
    val payerId: LiveData<Int> = _payerId

    private val _paymentOption = MutableLiveData<PaymentOptions>()
    val paymentOption = _paymentOption

    // Errors
    private val _errors = MutableLiveData<NewExpenseErrorModel>()
    val errors: LiveData<NewExpenseErrorModel> = _errors

    // Events
    private val _showMatchErrorDialog = MutableLiveData<EventWrapper<Double>>()
    val showMatchErrorDialog: LiveData<EventWrapper<Double>> = _showMatchErrorDialog

    private val _insertionDone = MutableLiveData<EventWrapper<Boolean>>()
    val insertonDone: LiveData<EventWrapper<Boolean>> = _insertionDone

    fun createExpenditure(
        tripId: Int,
        detail: String?,
        amountString: String
    ) {
        val payerId: Int? = _payerId.value
        val payerIdError = newExpenditureUseCase.checkPayerInputError(payerId)

        val amountInputError = newExpenditureUseCase.checkAmountInputError(amountString)
        val amountSpent: Double = amountString.toDoubleOrNull() ?: 0.0

        val paymentOption: PaymentOptions? = paymentOption.value

        val paymentOptionError: InputErrorType = newExpenditureUseCase.checkPaymentOptionError(paymentOption != null)

        val companions: List<CompanionModel> = _companions.value.orEmpty()
        val debtors: Map<Int, Double> = if (paymentOption == PaymentOptions.EQUALS) {
            newExpenditureUseCase.createEqualPayments(companions, amountSpent)
        } else {
            newExpenditureUseCase.completeDebtMap(paymentRelationship.value.orEmpty(), companions)
        }
        val debtorErrors: List<DebtorInputError> = newExpenditureUseCase.lookForDebtorInputErrors(
            totalAmount = amountSpent,
            debtors = debtors
        )
        if (
            debtorErrors.isNotEmpty()
            || amountInputError != InputErrorType.NO_ERROR
            || payerIdError != InputErrorType.NO_ERROR
        ) {
            emitErrors(debtorErrors, amountInputError, payerIdError, paymentOptionError)
            return
        }

        val amountsDifference = newExpenditureUseCase.checkAmountsDifference(amountSpent, debtors)
        val amountsMismatch = amountsDifference != 0.0
        if (amountsMismatch) {
            _showMatchErrorDialog.postValue(EventWrapper(amountsDifference))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            newExpenditureUseCase.addExpenditure(tripId, payerId!!, debtors, detail, amountSpent)
            _insertionDone.postValue(EventWrapper(true))
        }
    }

    private fun emitErrors(
        debtorErrors: List<DebtorInputError>,
        amountInputError: InputErrorType,
        payerIdError: InputErrorType,
        paymentOptionError: InputErrorType
    ) {
        val debtorErrorStates = mutableMapOf<Int, NewExpenseErrorStates>()
        debtorErrors.forEach { debtorInputError ->
            debtorErrorStates[debtorInputError.companionId] = debtorInputError.reason.toErrorState()
        }
        val newExpenseErrorModel = NewExpenseErrorModel(
            totalAmountError = amountInputError.toErrorState(),
            payerError = payerIdError.toErrorState(),
            paymentWayError = paymentOptionError.toErrorState(),
            paymentRelationshipErrors = debtorErrorStates
        )
        _errors.postValue(newExpenseErrorModel)
    }

    fun getCompanions(tripId: Int) {
        _companions.addSource(
            companionsUseCase.getTripCompanions(tripId).asLiveData(),
            Observer(_companions::postValue)
        )
    }

    /**
     * Add a user ID with an amount to the payment relationship for the expense.
     */
    fun addToPaymentRelationship(pair: Pair<Int, Double>) {
        val (id: Int, amount: Double) = pair
        paymentRelationship.modifyLiveDataMap {
            this[id] = amount
        }
    }

    fun setPayerId(id: Int) = _payerId.postValue(id)

    fun setPaymentOption(paymentOption: PaymentOptions) = _paymentOption.postValue(paymentOption)
}