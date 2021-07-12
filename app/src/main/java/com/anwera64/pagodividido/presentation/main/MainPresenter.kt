package com.anwera64.pagodividido.presentation.main

import com.anwera64.pagodividido.domain.models.Trip

class MainPresenter(private val view: MainDelegate) {

    fun getTrips() {
        //TODO()
    }

    interface MainDelegate {
        fun onTripsLoaded(trips: ArrayList<Trip>)
    }
}