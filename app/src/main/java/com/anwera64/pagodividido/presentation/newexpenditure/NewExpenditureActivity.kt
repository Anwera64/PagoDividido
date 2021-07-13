package com.anwera64.pagodividido.presentation.newexpenditure

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ActivityNewExpenditureBinding
import com.anwera64.pagodividido.domain.models.CompanionModel
import com.anwera64.pagodividido.presentation.base.BaseViewModelActivity
import com.anwera64.pagodividido.presentation.trip.TripActivity
import com.anwera64.pagodividido.utils.ViewUtils

class NewExpenditureActivity :
    BaseViewModelActivity<NewExpenditureViewModel, ActivityNewExpenditureBinding>(
        NewExpenditureViewModel::class
    ) {

    override val viewModelValue: Int? = null
    override val layout: Int = R.layout.activity_new_expenditure
    private val checkBoxes = ArrayList<CheckBox>()

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

    private fun loadSpinnerData(nameList: List<String>) {
        val arrayAdapter = ArrayAdapter(this, R.layout.list_metrial_drop_down_item, nameList)
        binding.tiPayer.setAdapter(arrayAdapter)
    }

    private fun createCompanionCheckBox(companion: CompanionModel) {
        val checkBox = CheckBox(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val margin = ViewUtils.gerMarginInDP(8, resources)
        layoutParams.setMargins(0, 0, 0, margin)

        checkBox.layoutParams = layoutParams
        checkBox.text = companion.name
        checkBox.tag = companion.uid.toInt()

        checkBoxes.add(checkBox)

        binding.llDebtors.addView(checkBox)
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

    private fun getSelectedDebtors(): ArrayList<Int>? {
        if (checkBoxes.isEmpty()) return null

        val result = ArrayList<Int>()
        checkBoxes.forEach { button ->
            if (button.isChecked) {
                result.add(button.tag as Int)
            }
        }

        return result
    }

    private fun createExpenditure(): Unit = with(binding) {
        val debtors = getSelectedDebtors() ?: return

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
        val payerId = tiPayer.text.toString()
        if (payerId.isEmpty()) {
            tilPayer.error = getString(R.string.error_no_payer_selected)
            tilPayer.isErrorEnabled = true
            return
        }
        tilPayer.isErrorEnabled = false
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

        val namesList = companions.map { companion ->
            createCompanionCheckBox(companion)
            companion.name
        }
        loadSpinnerData(namesList)
    }
}