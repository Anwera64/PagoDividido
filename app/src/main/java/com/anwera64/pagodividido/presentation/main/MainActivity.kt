package com.anwera64.pagodividido.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ActivityMainBinding
import com.anwera64.pagodividido.domain.models.TripModel
import com.anwera64.pagodividido.presentation.PagoDividioApp
import com.anwera64.pagodividido.presentation.base.BaseActivity
import com.anwera64.pagodividido.presentation.newtrip.NewTripActivity
import com.anwera64.pagodividido.presentation.trip.TripActivity

class MainActivity : BaseActivity<ActivityMainBinding>(), TripItemAdapter.Delegate {
    override val layout: Int
        get() {
            return R.layout.activity_main
        }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as PagoDividioApp).repository)
    }
    private val adapter: TripItemAdapter = TripItemAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        viewModel.trips.observe(this, Observer(this::onTripsLoaded))
    }

    private fun setupUi() = with(binding) {
        setSupportActionBar(toolbar)
        rvTrips.adapter = adapter
        btnNewTrip.setOnClickListener { createNewTrip() }
    }

    private fun createNewTrip() {
        Intent(this, NewTripActivity::class.java).also(this::startActivity)
    }

    override fun onTripPressed(uid: String, name: String) {
        Intent(this, TripActivity::class.java).run {
            putExtra("tripUid", uid)
            putExtra("name", name)
            startActivity(this)
        }
    }


    private fun onTripsLoaded(trips: List<TripModel>) {
        adapter.trips = trips
    }
}