package com.anwera64.pagodividido.presentation.trip

import com.anwera64.pagodividido.domain.models.Expenditure

class TripDetailPresenter(val view: TripDetailDelegate) {

    fun getTripDetails(tripUid: String) {
        //TODO
    }

    interface TripDetailDelegate {

        fun onTripDetailsReady(details: ArrayList<Expenditure>);

        fun onError(e: String)
    }
}