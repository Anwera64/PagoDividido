package com.anwera64.pagodividido.presentation.trip.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.FragmentTripDetailsBinding
import com.anwera64.pagodividido.domain.models.ExpenditureModel
import com.anwera64.pagodividido.presentation.base.BaseFragment
import com.anwera64.pagodividido.presentation.newexpenditure.NewExpenditureActivity
import com.anwera64.pagodividido.presentation.trip.TripDetailPresenter

class TripDetailFragment : BaseFragment<FragmentTripDetailsBinding>(),
    TripDetailPresenter.TripDetailDelegate {

    companion object {
        val instance = TripDetailFragment()
    }

    override val layout: Int = R.layout.fragment_trip_details
    private var expenditures = ArrayList<ExpenditureModel>()
    private val mPresenter = TripDetailPresenter(this)
    var tripUid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewDetails.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterTripDetail(expenditures)
        binding.recyclerViewDetails.adapter = adapter
        tripUid?.let(mPresenter::getTripDetails)
        binding.btnNewExpenditure.setOnClickListener { createNewTrip() }

    }

    private fun createNewTrip() {
        val intent = Intent(context, NewExpenditureActivity::class.java)
        intent.putExtra("tripUid", tripUid!!)
        startActivity(intent)
    }

    override fun onTripDetailsReady(details: ArrayList<ExpenditureModel>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(e: String) {
        Log.e(this.javaClass.name, e)
    }
}