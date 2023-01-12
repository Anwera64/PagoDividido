package com.anwera64.pagodividido.main

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.base.BaseComposeViewModelActivity
import com.anwera64.pagodividido.databinding.ActivityMainBinding
import com.anwera97.domain.models.TripModel
import com.anwera64.pagodividido.base.BaseViewModelActivity
import com.anwera64.pagodividido.newtrip.NewTripActivity
import com.anwera64.pagodividido.trip.TripActivity
import com.anwera64.pagodividido.utils.SwipeToDeleteCallback

class MainActivity :
    BaseComposeViewModelActivity<MainViewModel>(MainViewModel::class),
    TripItemAdapter.Delegate {

    private val adapter: TripItemAdapter = TripItemAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        val swipeCallback = SwipeToDeleteCallback(::onSwipeToDelete)
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
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
        viewModel.trips.observe(this, Observer(this::onTripsLoaded))
    }

    @Composable
    override fun Content() {
        val tripsState = viewModel.trips.observeAsState()
        TripsContent(
            trips = tripsState.value ?: emptyList(),
            createNewTripAction = ::createNewTrip,
            onTripSelected = ::onTripPressed
        )
    }
}