package com.anwera64.pagodividido.newtrip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anwera64.pagodividido.utils.EventWrapper
import com.anwera97.domain.models.TripShortModel
import com.anwera97.domain.usecases.CreateTripUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewTripViewModel(
    private val createTripUseCase: CreateTripUseCase
) : ViewModel() {

    private val _createdTrip = MutableLiveData<EventWrapper<TripShortModel>>()
    val createdTrip: LiveData<EventWrapper<TripShortModel>> = _createdTrip

    private val _companionLimitError = MutableLiveData<Boolean>()
    val companionLimitError: LiveData<Boolean> = _companionLimitError

    private val _nameError = MutableLiveData<Boolean>()
    val nameError: LiveData<Boolean> = _nameError

    fun createTrip(companions: List<String>, name: String) {
        val nameCorrect = createTripUseCase.checkTripName(name)
        _nameError.postValue(!nameCorrect)

        val sizeCorrect = createTripUseCase.checkCompanionsSize(companions)
        _companionLimitError.postValue(!sizeCorrect)

        if (!nameCorrect || !sizeCorrect) return
        viewModelScope.launch(Dispatchers.IO) {
            val trip = createTripUseCase.createTrip(companions, name)
            _createdTrip.postValue(EventWrapper(trip))
        }
    }
}