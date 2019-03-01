package com.anwera64.pagodividido.presentation.presenters

import com.anwera64.pagodividido.domain.FirebaseAdapter
import com.anwera64.pagodividido.domain.models.Companion
import java.util.*
import kotlin.collections.HashMap

class NewTripActivityPresenter(private val view: NewTripActivityDelegate) {

    private val firebase = FirebaseAdapter.instance
    private val db = firebase.db.reference
    private val userUID = firebase.mAuth.currentUser!!.uid

    fun createTrip(companions: HashMap<String, Companion>, name: String) {
        val uuid = UUID.randomUUID().toString()

        val values = HashMap<String, Any>()
        values["companions"] = companions
        values["totalSpent"] = 0
        values["name"] = name

        db.child("$userUID/$uuid").setValue(values)
                .addOnSuccessListener { view.onTripCreated() }
                .addOnFailureListener { view.onTripFailed() }
    }

    interface NewTripActivityDelegate {
        fun onTripCreated()

        fun onTripFailed()
    }
}