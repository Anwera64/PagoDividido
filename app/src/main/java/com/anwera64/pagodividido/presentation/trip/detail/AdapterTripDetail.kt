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
import com.anwera64.pagodividido.utils.CompanionsUtil.companionsToStrings
import com.anwera64.pagodividido.utils.DateFormatter
import kotlin.math.exp

class AdapterTripDetail : RecyclerView.Adapter<AdapterTripDetail.ViewHolder>() {

    var details: List<ExpenditureModel> = emptyList()
        set(value) {
            field = value
            //TODO update to use DiffResult
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DataBindingUtil.hñboasflbjfsbljkinflate<ListItemDetailBinding>(
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

        fun onBind(expenditure: ExpenditureModel) {
            binding.expenditure = expenditure
        }
    }
}