package com.anwera97.pagodividido.trip

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anwera97.pagodividido.utils.NOT_FOUND
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.domain.models.ResultModel
import com.anwera97.domain.usecases.CompanionResultUseCase
import com.anwera97.domain.usecases.CompanionsUseCase
import com.anwera97.domain.usecases.ExpenditureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    expenditureUseCase: ExpenditureUseCase,
    companionsUseCase: CompanionsUseCase,
    private val companionResultUseCase: CompanionResultUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tripId: Int = savedStateHandle.get<String>(TripActivity.TRIP_ID)?.toInt() ?: NOT_FOUND

    val expenses: StateFlow<List<ExpenditureModel>> = expenditureUseCase
        .getExpenditures(tripId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val companions: StateFlow<List<CompanionModel>> = companionsUseCase
        .getTripCompanions(tripId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _selectedCompanionId = MutableSharedFlow<Int>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentResult: StateFlow<ResultModel?> = _selectedCompanionId
        .flatMapLatest { companionId ->
            companionResultUseCase.getPayersWithDebtors(tripId, companionId)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        viewModelScope.launch {
            val firstCompanion = companions.first { it.isNotEmpty() }.first()
            _selectedCompanionId.emit(firstCompanion.uid.toInt())
        }
    }

    fun selectCompanion(companionUid: Int) {
        _selectedCompanionId.tryEmit(companionUid)
    }
}
