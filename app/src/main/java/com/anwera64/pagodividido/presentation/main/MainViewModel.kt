package com.anwera64.pagodividido.presentation.main

import androidx.lifecycle.*
import com.anwera64.pagodividido.data.entities.Trip
import com.anwera64.pagodividido.domain.models.TripModel
import com.anwera64.pagodividido.domain.repository.TripRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val tripRepository: TripRepository) : ViewModel() {

    val trips: LiveData<List<TripModel>> =
        tripRepository.allTrips.asLiveData(viewModelScope.coroutineContext)

    fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) { tripRepository.deleteTrip(id) }
    }
}