package com.anwera64.pagodividido.presentation.presenters

import com.anwera64.pagodividido.domain.FirebaseAdapter
import com.anwera64.pagodividido.domain.models.Expenditure

class TripDetailPresenter(view: TripDetailDelegate) {

    val dbRef = FirebaseAdapter.instance.db.reference
    val userID = FirebaseAdapter.instance.mAuth.currentUser!!.uid

    fun getTripDetails(tripUid: String) {

    }

    interface TripDetailDelegate {

        fun onTripDetailsReady(details: ArrayList<Expenditure>);

        fun onError(e: Error)
    }
}