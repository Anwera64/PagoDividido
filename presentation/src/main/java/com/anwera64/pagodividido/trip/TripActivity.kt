package com.anwera64.pagodividido.trip

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.anwera64.pagodividido.base.BaseComposeViewModelActivity
import com.anwera64.pagodividido.newexpenditure.NewExpenditureActivity
import com.anwera64.pagodividido.utils.NOT_FOUND

class TripActivity : BaseComposeViewModelActivity<TripViewModel>(TripViewModel::class) {

    companion object {
        const val TRIP_ID = "tripUid"
        const val NAME = "name"
    }

    private var tripId: Int = NOT_FOUND

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripId = intent.getStringExtra(TRIP_ID)?.toInt() ?: NOT_FOUND
    }

    private fun createNewTrip() {
        Intent(this, NewExpenditureActivity::class.java).run {
            putExtra(TRIP_ID, tripId)
            startActivity(this)
        }
    }

    @Composable
    override fun Content() {
        val expenses = viewModel.getExpenseList(tripId).observeAsState()
        val title = intent.getStringExtra(NAME)
        val sortedExpenses = expenses.value?.sortedByDescending { expense -> expense.date }
        TripView(
            backNavigation = ::finish,
            createNewExpenditure = ::createNewTrip,
            expenditures = sortedExpenses.orEmpty(),
            topBarTitle = title.orEmpty()
        )
    }

    override fun setupObservers() {
        //Not used
    }
}