package com.anwera64.pagodividido.presentation.trip.companionresult

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.FragmentTripCompanionResultBinding
import com.anwera64.pagodividido.domain.models.ResultModel
import com.anwera64.pagodividido.presentation.base.BaseViewModelFragment
import com.anwera64.pagodividido.presentation.trip.TripActivity

class CompanionResultFragment :
    BaseViewModelFragment<CompanionResultViewModel, FragmentTripCompanionResultBinding>(
        CompanionResultViewModel::class
    ) {

    companion object {
        fun getInstance(tripId: Int) = CompanionResultFragment().apply {
            arguments = bundleOf(TripActivity.TRIP_ID to tripId)
        }
    }

    override val layout: Int = R.layout.fragment_trip_companion_result
    override val viewModelValue: Int? = null

    override fun setupObservers() {
        arguments?.getInt(TripActivity.TRIP_ID)
            ?.let(viewModel::getItems)
            ?.observe(viewLifecycleOwner, ::observeResults)
    }

    private fun observeResults(results: List<ResultModel>?) {
        results ?: return
        (binding.rvResults.adapter as CompanionResultAdapter).results = results
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvResults.adapter = CompanionResultAdapter()
    }
}