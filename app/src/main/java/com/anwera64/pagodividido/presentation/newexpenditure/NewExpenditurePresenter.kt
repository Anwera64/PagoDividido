package com.anwera64.pagodividido.presentation.newexpenditure

import com.anwera64.pagodividido.domain.models.Companion
import java.util.*
import kotlin.collections.HashMap

class NewExpenditurePresenter(val view: NewExpenditureDelegate) {

    fun createExpenditure(
        ownerId: String,
        debtors: ArrayList<String>,
        detail: String,
        amountSpent: Float
    ) {
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
    }

    private fun createExpenditure(values: HashMap<String, Any>) {
        //TODO
    }

    private fun startUpdateTotalSpent(amountSpent: Float) {
        //TODO
    }

    private fun endUpdateTotalSpent(newAmount: Float) {
        //TODO
    }

    interface NewExpenditureDelegate {
        fun onExpenditureCreated()

        fun onError(e: String)

        fun onCompanionsObtained(companions: ArrayList<Companion>)
    }
}