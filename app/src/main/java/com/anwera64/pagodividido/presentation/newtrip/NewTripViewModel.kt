package com.anwera64.pagodividido.presentation.newtrip

import androidx.lifecycle.*
import com.anwera64.pagodividido.data.entities.Companion
import com.anwera64.pagodividido.data.entities.Trip
import com.anwera64.pagodividido.domain.repository.CompanionRepository
import com.anwera64.pagodividido.domain.repository.TripRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewTripViewModel(
    private val tripRepository: TripRepository,
    private val companionRepository: CompanionRepository
) : ViewModel() {

    private val _createdTrip = MutableLiveData<Trip>()
    val createdTrip: LiveData<Trip> = _createdTrip

    fun createTrip(companions: List<String>, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val tripId = tripRepository.insert(Trip(name)).toInt()
            companions.forEach { name ->
                companionRepository.insert(Companion(name, tripId))
            }
            _createdTrip.postValue(Trip(name, tripId))
        }
    }
}