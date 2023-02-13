package com.anwera64.pagodividido.newtrip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anwera64.pagodividido.utils.EventWrapper
import com.anwera64.pagodividido.utils.modifyLiveDataSet
import com.anwera97.domain.models.TripShortModel
import com.anwera97.domain.usecases.CreateTripUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTripViewModel @Inject constructor(
    private val createTripUseCase: CreateTripUseCase
) : ViewModel() {

    private val _createdTrip = MutableLiveData<EventWrapper<TripShortModel>>()
    val createdTrip: LiveData<EventWrapper<TripShortModel>> = _createdTrip

    val errors: MutableLiveData<Set<NewTripErrorStates>> = MutableLiveData()

    private val _companions = MutableLiveData<Set<String>>()
    val companions: LiveData<Set<String>> = _companions

    fun createTrip(name: String, companions: List<String>) {
        val errorStates: MutableSet<NewTripErrorStates> = mutableSetOf()
        val nameCorrect = createTripUseCase.checkTripName(name)
        if (!nameCorrect) {
            errorStates.add(NewTripErrorStates.EMPTY_TITLE)
        } else {
            errorStates.remove(NewTripErrorStates.EMPTY_TITLE)
        }

        val sizeCorrect = createTripUseCase.checkCompanionsSize(companions)
        if (!sizeCorrect) {
            errorStates.add(NewTripErrorStates.NOT_ENOUGH_COMPANIONS)
        } else {
            errorStates.remove(NewTripErrorStates.NOT_ENOUGH_COMPANIONS)
        }
        errors.postValue(errorStates)

        if (errorStates.isNotEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            val trip = createTripUseCase.createTrip(companions, name)
            _createdTrip.postValue(EventWrapper(trip))
        }
    }

    fun addCompanion(name: String) = _companions.modifyLiveDataSet { add(name) }

    fun removeCompanion(name: String) = _companions.modifyLiveDataSet { remove(name) }

}