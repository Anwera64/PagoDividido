package com.anwera64.pagodividido.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ActivityMainBinding
import com.anwera64.pagodividido.domain.models.TripModel
import com.anwera64.pagodividido.presentation.base.BaseViewModelActivity
import com.anwera64.pagodividido.presentation.newtrip.NewTripActivity
import com.anwera64.pagodividido.presentation.trip.TripActivity
import com.anwera64.pagodividido.utils.SwipeToDeleteCallback

class MainActivity :
    BaseViewModelActivity<MainViewModel, ActivityMainBinding>(MainViewModel::class),
    TripItemAdapter.Delegate {
    override val layout: Int
        get() = R.layout.activity_main

    override val viewModelValue: Int?
        get() = null

    private val adapter: TripItemAdapter = TripItemAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        viewModel.trips.observe(this, Observer(this::onTripsLoaded))
    }

    private fun setupUi() = with(binding) {
        setSupportActionBar(toolbar)
        rvTrips.adapter = adapter
        val swipeCallback = SwipeToDeleteCallback(::onSwipeToDelete)
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(rvTrips)
        btnNewTrip.setOnClickListener { createNewTrip() }
    }

    private fun onSwipeToDelete(position: Int) {
        viewModel.delete(adapter.trips[position].uid.toInt())
    }

    private fun createNewTrip() {
        Intent(this, NewTripActivity::class.java).also(this::startActivity)
    }

    override fun onTripPressed(uid: String, name: String) {
        Intent(this, TripActivity::class.java).run {
            putExtra(TripActivity.TRIP_ID, uid)
            putExtra(TripActivity.NAME, name)
            startActivity(this)
        }
    }


    private fun onTripsLoaded(trips: List<TripModel>) {
        adapter.trips = trips
    }

    override fun setupObservers() {
        //TODO("Not yet implemented")
    }
}