package com.anwera64.pagodividido.presentation.presenters

import android.util.Log
import com.anwera64.pagodividido.domain.FirebaseAdapter
import com.anwera64.pagodividido.domain.models.Companion
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
                p0.children.forEach{ tripSnap ->
                    val uid = tripSnap.key!!
                    val totalSpent = tripSnap.child("totalSpent").value as String
                    val name = tripSnap.child("name").value as String
                    val companions = ArrayList<Companion>()
                }
            }

        })
    }

    interface MainDelegate {
        fun onTripsLoaded()
    }
}