package com.anwera64.pagodividido.newtrip

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.Observer
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.base.BaseComposeViewModelActivity
import com.anwera64.pagodividido.trip.TripActivity
import com.anwera64.pagodividido.utils.EventWrapper
import com.anwera64.pagodividido.utils.nullOrHandled
import com.anwera97.domain.models.TripShortModel
import com.google.android.material.chip.Chip


class NewTripActivity : BaseComposeViewModelActivity<NewTripViewModel>(NewTripViewModel::class) {

    private fun observeCreatedTrip(eventWrapper: EventWrapper<TripShortModel>?) {
        if (eventWrapper.nullOrHandled()) return
        val trip = eventWrapper.getContentIfHandled()
        trip?.run { onTripCreated(id, name) } ?: onTripFailed()
    }

    private fun onTripCreated(uid: Int, name: String) {
        Intent(this, TripActivity::class.java)
            .putExtra(TripActivity.TRIP_ID, uid.toString())
            .putExtra(TripActivity.NAME, name)
            .let(::startActivity)
    }

    private fun onTripFailed() {
        Log.e("New Trip", "We failed to create the trip")
        // TODO() Show Error Message
    }

    override fun setupObservers() {
        viewModel.createdTrip.observe(this, Observer(::observeCreatedTrip))
    }

    @Composable
    override fun Content() {
        val companionsState: State<Set<String>?> = viewModel.companions.observeAsState()
        // Sort by name length to optimize space usage
        val companionList: List<String>? = companionsState.value
            ?.toList()
            ?.sortedBy { name -> name.length }
        val errorsState = viewModel.errors.observeAsState()
        val errors = errorsState.value ?: emptySet()
        NewTripContent(
            companionList ?: emptyList(),
            onDeleteCompanion = viewModel::removeCompanion,
            onCreateTrip = viewModel::createTrip,
            onCreateCompanion = viewModel::addCompanion,
            errors = errors,
            backNavigation = ::finish
        )
    }
}