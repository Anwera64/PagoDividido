package com.anwera64.pagodividido.newexpenditure

import android.app.AlertDialog
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.Observer
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.base.BaseComposeViewModelActivity
import com.anwera64.pagodividido.trip.TripActivity
import com.anwera64.pagodividido.utils.EventWrapper
import com.anwera64.pagodividido.utils.nullOrHandled
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewExpenditureActivity : BaseComposeViewModelActivity<NewExpenditureViewModel>() {

    override val viewModel: NewExpenditureViewModel by viewModels()
    override fun setupObservers() {
        viewModel.getCompanions(getTripId())
        viewModel.showMatchErrorDialog.observe(this, Observer(::observeMismatchAlertDialog))
        viewModel.insertonDone.observe(this, Observer(::onInsertionDone))
    }

    private fun observeMismatchAlertDialog(eventWrapper: EventWrapper<Double>?) {
        if (eventWrapper.nullOrHandled()) return
        eventWrapper.getContentIfHandled()?.let(::showAmountsMismatchDialog)
    }

    private fun showAmountsMismatchDialog(missingAmount: Double) {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_title_amount_not_reached)
            .setMessage(getString(R.string.dialog_message_amount_not_reached, missingAmount))
            .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun onInsertionDone(eventWrapper: EventWrapper<Boolean>) {
        if (eventWrapper.nullOrHandled()) return
        if (eventWrapper.getContentIfHandled() == true) finish()
    }

    @Composable
    override fun Content() {
        val companionListState = viewModel.companions.observeAsState()
        val chosenPayerIdState = viewModel.payerId.observeAsState()
        val paymentOption = viewModel.paymentOption.observeAsState()
        val errorModel = viewModel.errors.observeAsState()
        NewExpenditureContent(
            companionList = companionListState.value.orEmpty(),
            payerChosen = chosenPayerIdState.value,
            paymentOption = paymentOption.value,
            errorModel = errorModel.value,
            onPayerChosen = viewModel::setPayerId,
            onAmountSet = viewModel::addToPaymentRelationship,
            onOptionChanged = viewModel::setPaymentOption,
            createExpense = { detail, amount ->
                viewModel.createExpenditure(getTripId(), detail, amount)
            },
            backNavigation = { finish() }
        )
    }

    private fun getTripId() = intent.getIntExtra(TripActivity.TRIP_ID, -1)
}