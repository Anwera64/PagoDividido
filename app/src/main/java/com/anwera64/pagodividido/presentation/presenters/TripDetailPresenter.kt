package com.anwera64.pagodividido.presentation.presenters

import com.anwera64.pagodividido.domain.FirebaseAdapter
import com.anwera64.pagodividido.domain.models.Expenditure
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TripDetailPresenter(val view: TripDetailDelegate) {

    private val dbRef = FirebaseAdapter.instance.db.reference
    private val userID = FirebaseAdapter.instance.mAuth.currentUser!!.uid

    fun getTripDetails(tripUid: String) {
        dbRef.child("$userID/$tripUid/expenditures").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                view.onError(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {

            }
        })
    }

    interface TripDetailDelegate {

        fun onTripDetailsReady(details: ArrayList<Expenditure>);

        fun onError(e: String)
    }
}