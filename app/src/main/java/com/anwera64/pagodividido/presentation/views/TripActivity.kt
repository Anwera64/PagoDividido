package com.anwera64.pagodividido.presentation.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.utils.viewPager.SimplePagerAdapter
import com.anwera64.pagodividido.utils.viewPager.TabLayoutHelper
import kotlinx.android.synthetic.main.activity_trip.*

class TripActivity: AppCompatActivity() {

    private val pagerAdapter = SimplePagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        viewPagerTrips.adapter = pagerAdapter

        //Add Fragments

        updatePagerAdapter()
        updatetabs()
    }

    fun updatePagerAdapter() {
        pagerAdapter.notifyDataSetChanged()
    }

    fun updatetabs() {
        tabLayoutTrips.removeAllTabs()
        if (pagerAdapter.count > 1) {
            tabLayoutTrips.visibility = android.view.View.VISIBLE
            TabLayoutHelper.setupWithViewPager(tabLayoutTrips, viewPagerTrips)
        } else {
            tabLayoutTrips.visibility = android.view.View.GONE
        }
    }
}