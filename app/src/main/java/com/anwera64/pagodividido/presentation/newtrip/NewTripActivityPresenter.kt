package com.anwera64.pagodividido.presentation.newtrip

import com.anwera64.pagodividido.domain.models.CompanionModel

class NewTripActivityPresenter(private val view: NewTripActivityDelegate) {

    fun createTrip(companions: HashMap<String, CompanionModel>, name: String) {
        val values = HashMap<String, Any>()
        values["companions"] = companions
        values["totalSpent"] = 0
        values["name"] = name
    }

    interface NewTripActivityDelegate {
        fun onTripCreated(uid: String, name: String)

        fun onTripFailed()
    }
}