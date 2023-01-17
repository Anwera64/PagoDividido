package com.anwera64.pagodividido.main

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.Observer
import com.anwera64.pagodividido.base.BaseComposeViewModelActivity
import com.anwera97.domain.models.TripModel
import com.anwera64.pagodividido.newtrip.NewTripActivity
import com.anwera64.pagodividido.trip.TripActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseComposeViewModelActivity<MainViewModel>(),
    TripItemAdapter.Delegate {

    override val viewModel: MainViewModel by viewModels()

    private val adapter: TripItemAdapter = TripItemAdapter(this)

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