package com.anwera64.pagodividido.presentation.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.domain.models.Companion
import com.anwera64.pagodividido.domain.models.Expenditure
import kotlinx.android.synthetic.main.list_item_detail.view.*

class AdapterTripDetail(private val details: ArrayList<Expenditure>, private val context: Context,
                        view: AdapterTripDetailDelegate): RecyclerView.Adapter<AdapterTripDetail.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_detail, p0, false))
    }

    override fun getItemCount(): Int {
        return details.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val detail = details[p1]
        p0.whoPayed.text = detail.owner.name
        p0.amount.text = detail.amountSpent.toString()
        p0.detail.text = detail.detail
        p0.debtors.text = companionsToStrings(detail.debtors)
    }

    private fun companionsToStrings(companions: ArrayList<Companion>): String {
        var result = ""

        companions.forEach { companion ->
            result += companion.name + " "
        }

        return result.trim()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount = view.tvAmountSpentDetail!!
        val whoPayed = view.tvWhoPayed!!
        val debtors = view.tvWhosInDebt!!
        val detail = view.tvDetail!!
    }

    //Por si alguna vez quiero hacer una pantalla de detalle y manejar el onClick
    interface AdapterTripDetailDelegate
}