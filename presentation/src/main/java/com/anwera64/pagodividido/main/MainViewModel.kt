package com.anwera64.pagodividido.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.anwera97.domain.models.TripModel
import com.anwera97.domain.usecases.TripsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val tripsUseCase: TripsUseCase) : ViewModel() {

    val trips: LiveData<List<TripModel>> by lazy {
        tripsUseCase.getAllTrips().asLiveData(viewModelScope.coroutineContext)
    }

    fun delete(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        tripsUseCase.deleteTrip(id)
    }

}