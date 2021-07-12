package com.anwera64.pagodividido.presentation.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.domain.models.Trip
import com.anwera64.pagodividido.presentation.newtrip.NewTripActivity
import com.anwera64.pagodividido.presentation.trip.TripActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(), MainPresenter.MainDelegate, AdapterTripItem.AdapterTripDelegate {

    private val mPresenter = MainPresenter(this)
    private lateinit var adapter: AdapterTripItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        rvTrips.layoutManager = LinearLayoutManager(this)
        adapter = AdapterTripItem(ArrayList(), this, this)
        rvTrips.adapter = adapter

        btnNewTrip.setOnClickListener{ createNewTrip() }

        mPresenter.getTrips()
    }

    private fun createNewTrip() {
        val intent = Intent(this, NewTripActivity::class.java)
        startActivity(intent)
    }

    override fun onTripPressed(uid: String, name: String) {
        val intent = Intent(this, TripActivity::class.java)
        intent.putExtra("tripUid", uid)
        intent.putExtra("name", name)
        startActivity(intent)
    }


    override fun onTripsLoaded(trips: ArrayList<Trip>) {
        adapter.trips = trips
        adapter.notifyDataSetChanged()
    }
}