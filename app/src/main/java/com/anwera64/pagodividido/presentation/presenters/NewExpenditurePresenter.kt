package com.anwera64.pagodividido.presentation.presenters

import com.anwera64.pagodividido.domain.FirebaseAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class NewExpenditurePresenter(val view: NewExpenditureDelegate, val tripUid: String) {

    private val dbRef = FirebaseAdapter.instance.db.reference
    private val userID = FirebaseAdapter.instance.mAuth.currentUser!!.uid

    fun createExpenditure(ownerId: String, debtors: ArrayList<String>, detail: String, amountSpent: Float) {
        val values = HashMap<String, Any>()
        values["ownerId"] = ownerId
        values["debtors"] = debtors
        values["detail"] = detail
        values["amountSpent"] = amountSpent
        values["date"] = Date()

        createExpenditure(values)
        startUpdateTotalSpent(amountSpent)
    }

    private fun createExpenditure(values: HashMap<String, Any>) {
        val uid = UUID.randomUUID().toString()
        dbRef.child("$userID/$tripUid/expenditures/$uid").setValue(values)
    }

    private fun startUpdateTotalSpent(amountSpent: Float) {
        dbRef.child("$userID/$tripUid/totalSpent").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                view.onError(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val oldAmount = p0.getValue(Float::class.java)!!
                endUpdateTotalSpent(oldAmount + amountSpent)
            }
        })
    }

    private fun endUpdateTotalSpent(newAmount: Float) {
        dbRef.child("$userID/$tripUid/totalSpent").setValue(newAmount)
                .addOnFailureListener { e -> view.onError(e.localizedMessage) }
        //Falta on Success
    }

    interface NewExpenditureDelegate {
        fun onExpenditureCreated()

        fun onError(e: String)
    }
}