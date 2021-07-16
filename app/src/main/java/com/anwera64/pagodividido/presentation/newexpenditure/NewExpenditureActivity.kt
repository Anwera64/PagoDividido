package com.anwera64.pagodividido.presentation.newexpenditure

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ActivityNewExpenditureBinding
import com.anwera64.pagodividido.domain.models.CompanionModel
import com.anwera64.pagodividido.presentation.base.BaseViewModelActivity
import com.anwera64.pagodividido.presentation.trip.TripActivity
import com.anwera64.pagodividido.utils.ViewUtils
import com.anwera64.pagodividido.utils.uicomponents.DebtorInputView

class NewExpenditureActivity :
    BaseViewModelActivity<NewExpenditureViewModel, ActivityNewExpenditureBinding>(
        NewExpenditureViewModel::class
    ) {

    companion object {
        private const val NOT_FOUND = -1
    }

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
    }

    private fun getTripId() = intent.getIntExtra(TripActivity.TRIP_ID, -1)

    private fun setupUi() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadSpinnerData(nameList: List<CompanionModel>) = with(binding.tiPayer) {
        val arrayAdapter = ArrayAdapter(context, R.layout.list_metrial_drop_down_item, nameList)
        setAdapter(arrayAdapter)
        doOnTextChanged { text, _, _, _ ->
            nameList.find { companion -> companion.name == text.toString() }
                ?.uid
                ?.toInt()
                ?.let { id -> payerId = id }
        }
    }

    private fun createCompanionCheckBox(companion: CompanionModel) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            val margin = ViewUtils.gerMarginInDP(8, resources)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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

    //TODO Cleanup code. Too much indentation
    private fun getSelectedDebtors(maxAmount: Double): HashMap<Int, Double>? {
        if (debtorInputs.isEmpty()) return null
        var totalDebt = 0.0
        val result = HashMap<Int, Double>()
        for (debtorInputView in debtorInputs) {
            if (!debtorInputView.isChecked()) {
                continue
            }
            val amount = debtorInputView.getAmount()
            when {
                amount <= 0.0 -> {
                    debtorInputView.setErrorEnabled(true)
                    debtorInputView.setError(R.string.error_debtor_amount_0)
                    return null
                }
                amount > maxAmount -> {
                    debtorInputView.setErrorEnabled(true)
                    debtorInputView.setError(R.string.error_debtor_amount_over_max)
                    return null
                }
                totalDebt + amount > maxAmount -> {
                    debtorInputView.setErrorEnabled(true)
                    debtorInputView.setError(R.string.error_debtor_total_amount_maxed)
                    return null
                }
            }
            debtorInputView.setErrorEnabled(false)
            result[debtorInputView.tag as Int] = amount
            totalDebt += amount
        }
        if (totalDebt != maxAmount) {
            val missingAmount = maxAmount - totalDebt
            AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title_amount_not_reached)
                .setMessage(getString(R.string.dialog_message_amount_not_reached, missingAmount))
                .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
            return null
        }
        return result
    }

    private fun createExpenditure(): Unit = with(binding) {
        val amountString = binding.tiAmount.text.toString()
        if (amountString.isEmpty()) {
            tilAmount.error = getString(R.string.error_empty_amount)
            tilAmount.isErrorEnabled = true
            return
        }
        if (amountString.toFloatOrNull() == null) {
            tilAmount.error = getString(R.string.error_non_numeral_amount)
            tilAmount.isErrorEnabled = true
            return
        }
        tilAmount.isErrorEnabled = false
        val detail = tiDetail.text.toString()
        if (payerId == -1) {
            tilPayer.error = getString(R.string.error_no_payer_selected)
            tilPayer.isErrorEnabled = true
            return
        }
        tilPayer.isErrorEnabled = false
        val debtors = getSelectedDebtors(amountString.toDouble()) ?: return
        viewModel.createExpenditure(
            getTripId(),
            payerId,
            debtors,
            detail,
            amountString.toDouble()
        )
        onBackPressed()
    }

    private fun onCompanionsObtained(companions: List<CompanionModel>?) {
        if (companions == null) return
        companions.also(::loadSpinnerData).forEach(::createCompanionCheckBox)
    }
}