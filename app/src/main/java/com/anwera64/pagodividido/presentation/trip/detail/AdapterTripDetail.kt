package com.anwera64.pagodividido.presentation.trip.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ListItemDetailBinding
import com.anwera64.pagodividido.domain.models.CompanionModel
import com.anwera64.pagodividido.domain.models.ExpenditureModel
import com.anwera64.pagodividido.utils.DateFormatter

class AdapterTripDetail : RecyclerView.Adapter<AdapterTripDetail.ViewHolder>() {

    var details: List<ExpenditureModel> = emptyList()
        set(value) {
            field = value
            //TODO update to use DiffResult
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DataBindingUtil.inflate<ListItemDetailBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_detail,
            parent,
            false
        ).let(::ViewHolder)
    }

    override fun getItemCount(): Int = details.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(details[position])
    }

    inner class ViewHolder(private val binding: ListItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(expenditure: ExpenditureModel) = with(binding) {
            tvWhoPayed.text = expenditure.payer.name
            tvAmountSpentDetail.text = expenditure.amountSpent.toString()
            if (expenditure.detail.isNullOrEmpty()) {
                tvDetail.visibility = View.GONE
                tvDetailTitle.visibility = View.GONE
            } else {
                tvDetail.text = expenditure.detail
            }
            val debtors = companionsToStrings(expenditure.debtors)
            if (debtors.isEmpty()) {
                tvWhosInDebt.visibility = View.GONE
                tvDebtorsTitle.visibility = View.GONE
            } else {
                tvWhosInDebt.visibility = View.VISIBLE
                tvDebtorsTitle.visibility = View.VISIBLE
                tvWhosInDebt.text = debtors
            }
            tvDate.text = DateFormatter.formatDate(expenditure.date)
        }

        private fun companionsToStrings(companions: List<CompanionModel>): String {
            var result = ""
            companions.forEachIndexed { index, companion ->
                val separator = if (index < companions.size - 1) ", " else ""
                result += "${companion.name}$separator"
            }
            return result.trim()
        }
    }
}