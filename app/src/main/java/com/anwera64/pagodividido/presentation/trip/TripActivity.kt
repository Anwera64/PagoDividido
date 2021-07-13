package com.anwera64.pagodividido.presentation.trip

import android.os.Bundle
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ActivityTripBinding
import com.anwera64.pagodividido.presentation.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TripActivity : BaseActivity<ActivityTripBinding>() {

    companion object {
        const val DETAIL_TAB_POSITION = 0
        const val RESULT_TAB_POSITION = 1
        const val TRIP_ID = "tripUid"
        const val NAME = "name"
    }

    override val layout: Int = R.layout.activity_trip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = intent.getStringExtra(NAME)
        }
        val tripId = intent.getStringExtra(TRIP_ID) ?: return
        binding.vpTrip.adapter = TripFragmentAdapter(this, tripId)
        TabLayoutMediator(binding.tabLayoutTrips, binding.vpTrip, this::setupTabs).attach()
    }

    private fun setupTabs(tab: TabLayout.Tab, position: Int) {
        if (position == DETAIL_TAB_POSITION) {
            tab.text = getString(R.string.details)
            tab.setIcon(R.drawable.ic_money)
        }

        if (position == RESULT_TAB_POSITION) {
            tab.text = getString(R.string.result)
            tab.setIcon(R.drawable.ic_people)
        }
    }
}