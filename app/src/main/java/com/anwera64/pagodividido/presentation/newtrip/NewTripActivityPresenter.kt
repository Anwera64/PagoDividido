package com.anwera64.pagodividido.presentation.newtrip

import com.anwera64.pagodividido.domain.FirebaseAdapter
import com.anwera64.pagodividido.domain.models.Companion
import java.util.*
import kotlin.collections.HashMap

class NewTripActivityPresenter(private val view: NewTripActivityDelegate) {

    private val firebase = FirebaseAdapter.instance
    private val db = firebase.db.reference
    private val userUID = firebase.mAuth.currentUser!!.uid

    fun createTrip(companions: HashMap<String, Companion>, name: String) {
        val uid = UUID.randomUUID().toString()

        val values = HashMap<String, Any>()
        values["companions"] = companions
        values["totalSpent"] = 0
        values["name"] = name

        db.child("$userUID/$uid").setValue(values)
                .addOnSuccessListener { view.onTripCreated(uid, name) }
                .addOnFailureListener { view.onTripFailed() }
    }

    interface NewTripActivityDelegate {
        fun onTripCreated(uid: String, name: String)

        fun onTripFailed()
    }
}