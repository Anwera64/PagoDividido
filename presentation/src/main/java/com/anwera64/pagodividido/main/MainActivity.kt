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
    BaseComposeViewModelActivity<MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()

    private fun createNewTrip() {
        Intent(this, NewTripActivity::class.java).also(this::startActivity)
    }

    private fun onTripPressed(uid: String, name: String) {
        Intent(this, TripActivity::class.java).run {
            putExtra(TripActivity.TRIP_ID, uid)
            putExtra(TripActivity.NAME, name)
            startActivity(this)
        }
    }

    override fun setupObservers() {
        // Not used
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