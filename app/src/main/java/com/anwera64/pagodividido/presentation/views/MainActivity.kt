package com.anwera64.pagodividido.presentation.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.presentation.adapters.AdapterTripItem
import com.anwera64.pagodividido.presentation.presenters.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(), MainPresenter.MainDelegate, AdapterTripItem.AdapterTripDelegate {

    private val mPresenter = MainPresenter(this)
    private lateinit var adapter: AdapterTripItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        rvTrips.layoutManager = LinearLayoutManager(this)
        adapter = AdapterTripItem(ArrayList(), this, this)
        rvTrips.adapter = adapter

        btnNewTrip.setOnClickListener{ createNewTrip() }

        mPresenter.getTrips()
    }

    private fun createNewTrip() {

    }

    override fun onTripPressed(uid: String) {

    }


    override fun onTripsLoaded() {

    }
}