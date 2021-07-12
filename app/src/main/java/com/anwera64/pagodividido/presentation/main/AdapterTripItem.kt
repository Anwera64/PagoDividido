package com.anwera64.pagodividido.presentation.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ListItemTripBinding
import com.anwera64.pagodividido.domain.models.Trip

class AdapterTripItem(var trips: ArrayList<Trip>, private val view: AdapterTripDelegate) :
    RecyclerView.Adapter<AdapterTripItem.ViewHolder>() {

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

        holder.totalSpent.text = trip.totalSpent.toString()

        var companions = String()
        ArrayList(trip.companions.values).forEachIndexed { i, companion ->
            when {
                i > 0 -> companions += ", " + companion.name
                i == trip.companions.size - 1 -> companions += " and " + companion.name
                else -> companions = companion.name
            }
        }

        holder.companions.text = companions
        holder.itemView.setOnClickListener { view.onTripPressed(trip.uid, trip.name) }
    }

    class ViewHolder(binding: ListItemTripBinding) : RecyclerView.ViewHolder(binding.root) {
        //Migrate logic inside
        val totalSpent = binding.tvTotalSpentPreview
        val companions = binding.tvCompanions
    }

    interface AdapterTripDelegate {
        fun onTripPressed(uid: String, name: String)
    }
}