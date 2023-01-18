package com.anwera64.pagodividido.trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.domain.models.ResultModel
import com.anwera97.domain.usecases.CompanionResultUseCase
import com.anwera97.domain.usecases.CompanionsUseCase
import com.anwera97.domain.usecases.ExpenditureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val expenditureUseCase: ExpenditureUseCase,
    private val companionsUseCase: CompanionsUseCase,
    private val companionResultUseCase: CompanionResultUseCase
) : ViewModel() {

    private val _currentResult = MediatorLiveData<ResultModel>()
    /**
     * Allows the removal of an old source of livedata to prevent errors
     */
    private var oldSource: LiveData<ResultModel?>? = null
    val currentResult: LiveData<ResultModel> = _currentResult

    fun getExpenseList(tripUid: Int): LiveData<List<ExpenditureModel>> {
        return expenditureUseCase.getExpenditures(tripUid).asLiveData()
    }

    fun getResultsForCompanion(tripId: Int, companionUid: Int) {
        val source = companionResultUseCase.getPayersWithDebtors(tripId, companionUid).asLiveData()
        oldSource?.let(_currentResult::removeSource)
        _currentResult.addSource(source, Observer(_currentResult::postValue))
        oldSource = source
    }

    fun getCompnaions(tripId: Int): LiveData<List<CompanionModel>> {
        return companionsUseCase.getTripCompanions(tripId).asLiveData()
    }
}