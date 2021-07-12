package com.anwera64.pagodividido.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ActivityMainBinding
import com.anwera64.pagodividido.domain.models.Trip
import com.anwera64.pagodividido.presentation.base.BaseActivity
import com.anwera64.pagodividido.presentation.newtrip.NewTripActivity
import com.anwera64.pagodividido.presentation.trip.TripActivity

class MainActivity : BaseActivity<ActivityMainBinding>(), MainPresenter.MainDelegate,
    AdapterTripItem.AdapterTripDelegate {
    override val layout: Int
        get() {
            return R.layout.activity_main
        }
    private val mPresenter = MainPresenter(this)
    private lateinit var adapter: AdapterTripItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        binding.rvTrips.layoutManager = LinearLayoutManager(this)
        adapter = AdapterTripItem(ArrayList(), this)
        binding.rvTrips.adapter = adapter
        binding.btnNewTrip.setOnClickListener { createNewTrip() }

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