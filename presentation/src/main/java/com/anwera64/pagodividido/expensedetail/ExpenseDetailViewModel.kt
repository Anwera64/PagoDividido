package com.anwera64.pagodividido.expensedetail

import androidx.lifecycle.*
import com.anwera97.domain.models.ExpenditureDetailModel
import com.anwera97.domain.usecases.ExpenditureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val expenditureUseCase: ExpenditureUseCase,
) : ViewModel() {

    private val _expenditureModel = MediatorLiveData<ExpenditureDetailModel>()
    val expenseModel: LiveData<ExpenditureDetailModel> = _expenditureModel

    /**
     * This should only be done once, so no need to remove the old source because it will not exist.
     */
    fun getExpense(id: Int) {
        val dataSource = expenditureUseCase
            .getExpediture(id)
            .asLiveData(viewModelScope.coroutineContext)
        _expenditureModel.addSource(dataSource, _expenditureModel::postValue)
    }
}