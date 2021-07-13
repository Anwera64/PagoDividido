package com.anwera64.pagodividido.presentation.trip

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.anwera64.pagodividido.presentation.trip.companionresult.TripCompanionResultFragment
import com.anwera64.pagodividido.presentation.trip.detail.TripDetailFragment

class TripFragmentAdapter(activity: FragmentActivity, private val tripId: String) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            TripActivity.DETAIL_TAB_POSITION -> TripDetailFragment.getInstance(tripId.toInt())
            TripActivity.RESULT_TAB_POSITION -> TripCompanionResultFragment()
            else -> throw IndexOutOfBoundsException("Out of bounds for this Adapter")
        }
    }
}