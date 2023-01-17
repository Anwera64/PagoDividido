package com.anwera64.pagodividido.newexpenditure

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.base.BaseViewModelActivity
import com.anwera64.pagodividido.databinding.ActivityNewExpenditureBinding
import com.anwera64.pagodividido.trip.TripActivity
import com.anwera64.pagodividido.utils.EventWrapper
import com.anwera64.pagodividido.utils.NOT_FOUND
import com.anwera64.pagodividido.utils.ViewUtils
import com.anwera64.pagodividido.utils.nullOrHandled
import com.anwera64.pagodividido.utils.uicomponents.DebtorInputView
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.DebtorInputError
import com.anwera97.domain.models.DebtorInputErrorReasons
import com.anwera97.domain.models.InputErrorTypes
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewExpenditureActivity :
    BaseViewModelActivity<NewExpenditureViewModel, ActivityNewExpenditureBinding>() {

    override val viewModel: NewExpenditureViewModel by viewModels()
    override val viewModelValue: Int? = null
    override val layout: Int = R.layout.activity_new_expenditure
    private val debtorInputs = ArrayList<DebtorInputView>()
    private var payerId = NOT_FOUND

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        viewModel.getCompanions(getTripId())
    }

    override fun setupObservers() {
        viewModel.companions.observe(this, Observer(::onCompanionsObtained))
        viewModel.debtInputErrors.observe(this, Observer(::observeDebtorInputErrors))
        viewModel.showMatchErrorDialog.observe(this, Observer(::observeMismatchAlertDialog))
        viewModel.amountInputError.observe(this, Observer(::setAmountInputError))
        viewModel.payerInputError.observe(this, Observer(::setPayerInputError))
        viewModel.insertonDone.observe(this, Observer(::onInsertionDone))
    }

    private fun onInsertionDone(eventWrapper: EventWrapper<Boolean>) {
        if (eventWrapper.nullOrHandled()) return
        if (eventWrapper.getContentIfHandled() == true) finish()
    }

    private fun getTripId() = intent.getIntExtra(TripActivity.TRIP_ID, -1)

    private fun setupUi() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadSpinnerData(nameList: List<CompanionModel>) = with(binding.tiPayer) {
        val arrayAdapter = ArrayAdapter(context, R.layout.list_metrial_drop_down_item, nameList)
        setAdapter(arrayAdapter)
        doOnTextChanged { text, _, _, _ -> onPayerInputTextChanged(nameList, text) }
    }

    private fun onPayerInputTextChanged(nameList: List<CompanionModel>, text: CharSequence?) {
        nameList.find { companion -> companion.name == text.toString() }
                ?.uid
                ?.toInt()
                ?.let { id -> payerId = id }
    }

    private fun createCompanionCheckBox(companion: CompanionModel) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            val margin = ViewUtils.gerMarginInDP(8f, resources)
            setMargins(0, 0, 0, margin)
        }
        DebtorInputView(this).apply {
            this.layoutParams = layoutParams
            name = companion.name
            tag = companion.uid.toInt()
        }.also { inputView ->
            debtorInputs.add(inputView)
            binding.llDebtors.addView(inputView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_create, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.create -> createExpenditure()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun observeDebtorInputErrors(errors: List<DebtorInputError>?) {
        if (errors == null) return
        for (error in errors) {
            val errorId = when (error.reason) {
                DebtorInputErrorReasons.ZERO_OR_NEGATIVE -> R.string.error_debtor_amount_0
                DebtorInputErrorReasons.OVER_MAX_AMOUNT -> R.string.error_debtor_amount_over_max
                DebtorInputErrorReasons.SUM_EXCEEDS_MAX_AMOUNT -> R.string.error_debtor_amount_over_max
            }
            val view: DebtorInputView =
                binding.llDebtors.findViewWithTag(error.viewTag) as? DebtorInputView ?: continue
            setDebtorInputError(view, errorId)
        }
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

    private fun getSelectedDebtors(): Map<Int, Double> {
        if (debtorInputs.isEmpty()) return emptyMap()
        val result = HashMap<Int, Double>()
        for (debtorInputView in debtorInputs) {
            if (!debtorInputView.isChecked()) continue
            debtorInputView.setErrorEnabled(false)
            result[debtorInputView.tag as Int] = debtorInputView.getAmount()
        }
        return result
    }

    private fun setDebtorInputError(view: DebtorInputView, @StringRes error: Int) = with(view) {
        setErrorEnabled(true)
        setError(error)
    }

    private fun setAmountInputError(errorType: InputErrorTypes?) {
        setInputError(errorType, binding.tilAmount)
    }

    private fun setInputError(errorType: InputErrorTypes?, view: TextInputLayout) = with(view) {
        if (errorType == InputErrorTypes.NO_ERROR) {
            isErrorEnabled = false
            return
        }
        isErrorEnabled = true
        error = when (errorType) {
            InputErrorTypes.INPUT_NUMBER -> getString(R.string.error_non_numeral_amount)
            InputErrorTypes.INPUT_AMOUNT -> getString(R.string.error_empty_amount)
            InputErrorTypes.INEXISTENT_ID -> getString(R.string.error_no_payer_selected)
            else -> getString(R.string.default_error)
        }
    }

    private fun setPayerInputError(errorType: InputErrorTypes?) {
        setInputError(errorType, binding.tilPayer)
    }

    private fun createExpenditure(): Unit = with(binding) {
        val amountString = binding.tiAmount.text.toString()
        val detail = tiDetail.text.toString()
        val debtors = getSelectedDebtors()
        viewModel.createExpenditure(getTripId(), payerId, debtors, detail, amountString)
    }

    private fun onCompanionsObtained(companions: List<CompanionModel>?) {
        if (companions == null) return
        companions.also(::loadSpinnerData).forEach(::createCompanionCheckBox)
    }
}