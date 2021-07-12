package com.anwera64.pagodividido.presentation.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ListItemTripBinding
import com.anwera64.pagodividido.domain.models.TripModel

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
        val trip = trips[position]

        holder.onBind(trip)

    }

    inner class ViewHolder(private val binding: ListItemTripBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(trip: TripModel) = with(binding) {
            tvTotalSpentPreview.text = trip.totalSpent.toString()

            var companions = String()
            ArrayList(trip.companions.values).forEachIndexed { i, companion ->
                when {
                    i > 0 -> companions += ", ${companion.name}"
                    i == trip.companions.size - 1 -> companions += " and ${companion.name}"
                    else -> companions = companion.name
                }
            }

            tvCompanions.text = companions
            itemView.setOnClickListener { view.onTripPressed(trip.uid, trip.name) }
        }
    }

    interface Delegate {
        fun onTripPressed(uid: String, name: String)
    }
}