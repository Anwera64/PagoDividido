package com.anwera64.pagodividido.presentation.presenters

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainPresenter(private val view: MainDelegate) {

    private val dbRef = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    fun getTrips() {

    }

    interface MainDelegate {
        fun onTripsLoaded()
    }
}