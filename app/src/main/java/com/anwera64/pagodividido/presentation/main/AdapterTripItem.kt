package com.anwera64.pagodividido.presentation.main

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.domain.models.Trip
import kotlinx.android.synthetic.main.list_item_trip.view.*

class AdapterTripItem(
    var trips: ArrayList<Trip>, private val context: Context,
    private val view: AdapterTripDelegate
) : RecyclerView.Adapter<AdapterTripItem.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_trip, p0, false))
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val trip = trips[p1]

        p0.totalSpent.text = trip.totalSpent.toString()

        var companions = String()
        ArrayList(trip.companions.values).forEachIndexed { i, companion ->
            when {
                i > 0 -> companions += ", " + companion.name
                i == trip.companions.size - 1 -> companions += " and " + companion.name
                else -> companions = companion.name
            }
        }

        p0.companions.text = companions
        p0.itemView.setOnClickListener { view.onTripPressed(trip.uid, trip.name) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val totalSpent = view.tvTotalSpentPreview!!
        val companions = view.tvCompanions!!
    }

    interface AdapterTripDelegate {
        fun onTripPressed(uid: String, name: String)
    }
}