package com.anwera64.pagodividido.presentation.presenters

import com.anwera64.pagodividido.domain.FirebaseAdapter

class MainPresenter(private val view: MainDelegate) {

    private val firebase = FirebaseAdapter.instance
    private val db = firebase.db.reference
    private val userUID = firebase.mAuth.currentUser!!.uid

    fun getTrips() {

    }

    interface MainDelegate {
        fun onTripsLoaded()
    }
}