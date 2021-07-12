package com.anwera64.pagodividido.presentation.trip.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.domain.models.Expenditure
import com.anwera64.pagodividido.presentation.newexpenditure.NewExpenditureActivity
import com.anwera64.pagodividido.presentation.trip.TripDetailPresenter
import kotlinx.android.synthetic.main.fragment_trip_details.*

class TripDetailFragment : Fragment(), TripDetailPresenter.TripDetailDelegate {

    companion object {
        val instance = TripDetailFragment()
    }

    private var expenditures = ArrayList<Expenditure>()
    private val mPresenter = TripDetailPresenter(this)
    var tripUid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trip_details, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            recyclerViewDetails.layoutManager = LinearLayoutManager(context)
            val adapter = AdapterTripDetail(expenditures, it)
            recyclerViewDetails.adapter = adapter
        }

        tripUid?.let {
            mPresenter.getTripDetails(it)
            btnNewExpenditure.setOnClickListener { createNewTrip() }
        }


    }


    private fun createNewTrip() {
        val intent = Intent(context, NewExpenditureActivity::class.java)
        intent.putExtra("tripUid", tripUid!!)
        startActivity(intent)
    }

    override fun onTripDetailsReady(details: ArrayList<Expenditure>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(e: String) {
        Log.e(this.javaClass.name, e)
    }
}