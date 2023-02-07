package com.anwera64.pagodividido.expensedetail

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.anwera64.pagodividido.base.BaseComposeViewModelActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenseDetailActivity : BaseComposeViewModelActivity<ExpenseDetailViewModel>() {

    companion object {
        const val EXPENSE_ID = "Expenditure ID"
    }

    override val viewModel: ExpenseDetailViewModel by viewModels()

    override fun setupObservers() {
        val expenseId = intent.getIntExtra(EXPENSE_ID, -1)
        viewModel.getExpense(expenseId)
    }

    @Composable
    override fun Content() {
        val expenseModelState = viewModel.expenseModel.observeAsState()
        ExpenseDetailContent(
            expenseModel = expenseModelState.value,
            backNavigation = ::finish
        )
    }
}