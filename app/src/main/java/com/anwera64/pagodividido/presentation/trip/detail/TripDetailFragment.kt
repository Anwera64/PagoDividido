package com.anwera64.pagodividido.presentation.trip.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.FragmentTripDetailsBinding
import com.anwera64.pagodividido.domain.models.ExpenditureModel
import com.anwera64.pagodividido.presentation.base.BaseViewModelFragment
import com.anwera64.pagodividido.presentation.newexpenditure.NewExpenditureActivity
import com.anwera64.pagodividido.presentation.trip.TripActivity
import com.anwera64.pagodividido.presentation.trip.TripViewModel

class TripDetailFragment :
    BaseViewModelFragment<TripViewModel, FragmentTripDetailsBinding>(TripViewModel::class) {

    companion object {
        fun getInstance(tripId: Int): TripDetailFragment = TripDetailFragment().apply {
            arguments = bundleOf(
                TripActivity.TRIP_ID to tripId
            )
        }
    }

    override val viewModelValue: Int? = null
    override val layout: Int = R.layout.fragment_trip_details
    var tripUid: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    override fun setupObservers() {
        arguments?.getInt(TripActivity.TRIP_ID)?.let { id ->
            tripUid = id
            viewModel.getTripDetails(id).observe(viewLifecycleOwner, Observer(::onTripDetailsReady))
        }
    }

    private fun setupUi() = with(binding) {
        btnNewExpenditure.setOnClickListener { createNewTrip() }
        rvDetails.adapter = AdapterTripDetail()
    }

    private fun createNewTrip() {
        Intent(context, NewExpenditureActivity::class.java).run {
            putExtra(TripActivity.TRIP_ID, tripUid)
            startActivity(this)
        }
    }

    private fun onTripDetailsReady(details: List<ExpenditureModel>?) {
        if (details == null) return
        (binding.rvDetails.adapter as AdapterTripDetail).details = details
    }
}