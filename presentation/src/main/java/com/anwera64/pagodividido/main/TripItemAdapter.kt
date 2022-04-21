package com.anwera64.pagodividido.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ListItemTripBinding
import com.anwera97.domain.models.TripModel

class TripItemAdapter(private val view: Delegate) :
        RecyclerView.Adapter<TripItemAdapter.ViewHolder>() {

    var trips: List<TripModel> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DataBindingUtil.inflate<ListItemTripBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_item_trip,
                parent,
                false
        ).let(::ViewHolder)
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(trips[position])
    }

    inner class ViewHolder(private val binding: ListItemTripBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun onBind(trip: TripModel) = with(binding) {
            tvTotalSpentPreview.text = trip.totalSpent.toString()
            var companions = String()
            trip.companions.forEachIndexed { i, companion ->
                when (i) {
                    0 -> companions = companion.name
                    trip.companions.size - 1 -> companions += " and ${companion.name}"
                    else -> companions += ", ${companion.name}"
                }
            }
            tvCompanions.text = companions
            itemView.setOnClickListener { view.onTripPressed(trip.uid, trip.name) }
            tvTitle.text = trip.name
        }
    }

    interface Delegate {
        fun onTripPressed(uid: String, name: String)
    }
}