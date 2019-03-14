package com.anwera64.pagodividido.presentation.presenters

import com.anwera64.pagodividido.domain.FirebaseAdapter
import com.anwera64.pagodividido.domain.models.Companion
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList
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

    fun getCompanions() {
        dbRef.child("$userID/$tripUid/companions").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                view.onError(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val companions = ArrayList<Companion>()
                p0.children.forEach { childSnap ->
                    val cName = childSnap.child("name").value as String
                    val cTotalDebt = childSnap.child("totalDebt").getValue(Float::class.java)!!
                    val cTotalOwned = childSnap.child("totalOwed").getValue(Float::class.java)!!

                    val companion = Companion(childSnap.key.toString(), cName, cTotalDebt, cTotalOwned)
                    companions.add(companion)
                }

                view.onCompanionsObtained(companions)
            }

        })
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

        fun onCompanionsObtained(companions: ArrayList<Companion>)
    }
}