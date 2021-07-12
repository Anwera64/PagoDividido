package com.anwera64.pagodividido.presentation.trip.detail

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ListItemDetailBinding
import com.anwera64.pagodividido.domain.models.CompanionModel
import com.anwera64.pagodividido.domain.models.ExpenditureModel

class AdapterTripDetail(private val details: ArrayList<ExpenditureModel>) :
    RecyclerView.Adapter<AdapterTripDetail.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DataBindingUtil.inflate<ListItemDetailBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_detail,
            parent,
            false
        ).let(::ViewHolder)
    }

    override fun getItemCount(): Int {
        return details.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = details[position]
        holder.whoPayed.text = detail.payer.name
        holder.amount.text = detail.amountSpent.toString()
        holder.detail.text = detail.detail
        holder.debtors.text = companionsToStrings(detail.debtors)
    }

    private fun companionsToStrings(companions: ArrayList<CompanionModel>): String {
        var result = ""

        companions.forEach { companion ->
            result += companion.name + " "
        }

        return result.trim()
    }

    class ViewHolder(binding: ListItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        val amount = binding.tvAmountSpentDetail
        val whoPayed = binding.tvWhoPayed
        val debtors = binding.tvWhosInDebt
        val detail = binding.tvDetail
    }
}