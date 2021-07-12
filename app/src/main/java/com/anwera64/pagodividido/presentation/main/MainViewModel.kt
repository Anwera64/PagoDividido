package com.anwera64.pagodividido.presentation.main

import androidx.lifecycle.*
import com.anwera64.pagodividido.domain.models.TripModel
import com.anwera64.pagodividido.domain.repository.TripRepository

class MainViewModel(tripRepository: TripRepository) : ViewModel() {

    val trips: LiveData<List<TripModel>> = tripRepository.allTrips.asLiveData(viewModelScope.coroutineContext)

}