package com.anwera64.pagodividido.presentation.presenters

import android.util.Log
import com.anwera64.pagodividido.domain.FirebaseAdapter
import com.anwera64.pagodividido.domain.models.Companion
import com.anwera64.pagodividido.domain.models.Expenditure
import com.anwera64.pagodividido.domain.models.Trip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainPresenter(private val view: MainDelegate) {

    private val firebase = FirebaseAdapter.instance
    private val dbRef = firebase.db.reference
    private val userUID = firebase.mAuth.currentUser!!.uid

    fun getTrips() {
        dbRef.child(userUID).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e(this.javaClass.name, p0.details)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val trips = ArrayList<Trip>()
                p0.children.forEach{ tripSnap ->
                    val uid = tripSnap.key as String
                    val totalSpent = tripSnap.child("totalSpent").getValue(Float::class.java)!!
                    val name = tripSnap.child("name").value as String
                    val companions = HashMap<String, Companion>()
                    tripSnap.child("companions").children.forEach { childSnap ->
                        val cName = childSnap.child("name").value as String
                        val cTotalDebt = childSnap.child("totalDebt").getValue(Float::class.java)!!
                        val cTotalOwned = childSnap.child("totalOwed").getValue(Float::class.java)!!

                        val companion = Companion(childSnap.key.toString(), cName, cTotalDebt, cTotalOwned)
                        companions[companion.uid] = companion
                    }
                    val expenditures = ArrayList<Expenditure>()
                    val trip = Trip(uid, totalSpent, name,  companions, expenditures)
                    trips.add(trip)
                }
                view.onTripsLoaded(trips)
            }

        })
    }

    interface MainDelegate {
        fun onTripsLoaded(trips: ArrayList<Trip>)
    }
}