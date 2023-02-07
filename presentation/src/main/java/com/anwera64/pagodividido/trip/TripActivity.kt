package com.anwera64.pagodividido.trip

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.anwera64.pagodividido.base.BaseComposeViewModelActivity
import com.anwera64.pagodividido.expensedetail.ExpenseDetailActivity
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

    private fun openExpenseDetail(id: Int) {
        val intent = Intent(this, ExpenseDetailActivity::class.java)
            .putExtra(ExpenseDetailActivity.EXPENSE_ID, id)
        startActivity(intent)
    }

    @Composable
    override fun Content() {
        val expenses = viewModel.getExpenseList(tripId).observeAsState()
        val title = intent.getStringExtra(NAME)
        val sortedExpenses = expenses.value?.sortedByDescending { expense -> expense.date }
        val companions = viewModel.getCompnaions(tripId).observeAsState()
        val resultModel = viewModel.currentResult.observeAsState()
        TripView(
            backNavigation = ::finish,
            createNewExpenditure = ::createNewTrip,
            expenditures = sortedExpenses.orEmpty(),
            topBarTitle = title.orEmpty(),
            companionList = companions.value.orEmpty(),
            requestCompanionResult = { id: String ->
                viewModel.getResultsForCompanion(tripId, id.toInt())
            },
            resultModel = resultModel.value,
            onExpenseSelected = ::openExpenseDetail
        )
    }

    override fun setupObservers() {
        //Not used
    }
}