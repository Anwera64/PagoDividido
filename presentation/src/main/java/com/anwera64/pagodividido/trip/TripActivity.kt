package com.anwera64.pagodividido.trip

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anwera64.pagodividido.base.BaseComposeViewModelActivity
import com.anwera64.pagodividido.newexpenditure.NewExpenditureActivity
import com.anwera64.pagodividido.utils.NOT_FOUND
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripActivity : BaseComposeViewModelActivity<TripViewModel>() {

    override val viewModel: TripViewModel by viewModels()

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
        val expenses by viewModel.expenses.collectAsStateWithLifecycle()
        val title = intent.getStringExtra(NAME)
        val companions by viewModel.companions.collectAsStateWithLifecycle()
        val resultModel by viewModel.currentResult.collectAsStateWithLifecycle()
        TripView(
            backNavigation = ::finish,
            createNewExpenditure = ::createNewTrip,
            expenditures = expenses,
            topBarTitle = title.orEmpty(),
            companionList = companions,
            requestCompanionResult = { id: String ->
                viewModel.selectCompanion(id.toInt())
            },
            resultModel = resultModel
        )
    }

    override fun setupObservers() {
        //Not used
    }
}
