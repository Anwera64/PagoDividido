package com.anwera97.pagodividido.main

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.anwera97.pagodividido.base.BaseComposeViewModelActivity
import com.anwera97.pagodividido.newtrip.NewTripActivity
import com.anwera97.pagodividido.trip.TripActivity
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