package com.anwera64.pagodividido.presentation.trip

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.presentation.trip.companionresult.TripCompanionResultFragment
import com.anwera64.pagodividido.presentation.trip.detail.TripDetailFragment
import com.anwera64.pagodividido.utils.viewPager.SimplePagerAdapter
import com.anwera64.pagodividido.utils.viewPager.TabLayoutHelper
import kotlinx.android.synthetic.main.activity_trip.*

class TripActivity: AppCompatActivity() {

    private val pagerAdapter = SimplePagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewPagerTrips.adapter = pagerAdapter

        if (intent.hasExtra("tripUid")) {
            val tripUid = intent.getStringExtra("tripUid")

            TripDetailFragment.instance.tripUid = tripUid
        }

        if (intent.hasExtra("name")) {
            val name = intent.getStringExtra("name")

            supportActionBar?.title = name
        }

        //Add Fragments
        pagerAdapter.addFragment(TripDetailFragment.instance, resources.getString(R.string.detalle), R.drawable.ic_money)
        pagerAdapter.addFragment(TripCompanionResultFragment.instance, "Resultado", R.drawable.ic_people)

        updatePagerAdapter()
        updatetabs()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updatePagerAdapter() {
        pagerAdapter.notifyDataSetChanged()
    }

    private fun updatetabs() {
        tabLayoutTrips.removeAllTabs()
        if (pagerAdapter.count > 1) {
            tabLayoutTrips.visibility = android.view.View.VISIBLE
            TabLayoutHelper.setupWithViewPager(tabLayoutTrips, viewPagerTrips)
        } else {
            tabLayoutTrips.visibility = android.view.View.GONE
        }
    }
}