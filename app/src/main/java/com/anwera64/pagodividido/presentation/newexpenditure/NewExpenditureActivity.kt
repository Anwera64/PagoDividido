package com.anwera64.pagodividido.presentation.newexpenditure

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ActivityNewExpenditureBinding
import com.anwera64.pagodividido.domain.models.CompanionModel
import com.anwera64.pagodividido.presentation.base.BaseActivity
import com.anwera64.pagodividido.presentation.trip.TripActivity
import com.anwera64.pagodividido.utils.ViewUtils

class NewExpenditureActivity : BaseActivity<ActivityNewExpenditureBinding>(), NewExpenditurePresenter.NewExpenditureDelegate {
    override val layout: Int = R.layout.activity_new_expenditure

    private var mPresenter: NewExpenditurePresenter? = null
    private val checkBoxes = ArrayList<CheckBox>()
    private var ownerUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_expenditure)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(TripActivity.TRIP_ID)) {
            val tripUid = intent.getStringExtra(TripActivity.TRIP_ID)
            mPresenter = NewExpenditurePresenter(this)

            mPresenter?.getCompanions()
        }
    }

    private fun loadSpinnerData(nameList: ArrayList<SpecialPair>) {
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nameList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spOwner.adapter = arrayAdapter
        binding.spOwner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selected = p0?.getItemAtPosition(p2) as SpecialPair
                ownerUid = selected.first
            }
        }
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
        checkBox.tag = companion.uid

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

    private fun getSelectedDebtors(): ArrayList<String>? {
        if (checkBoxes.isEmpty()) {
            Toast.makeText(this, "No companions", Toast.LENGTH_LONG).show()
            return null
        }

        val result = ArrayList<String>()
        checkBoxes.forEach { button ->
            if (button.isSelected) result.add(button.tag as String)
        }

        return result
    }

    private fun createExpenditure(): Unit = with(binding) {
        val debtors = getSelectedDebtors() ?: return

        val amountString = binding.tiAmount.text.toString()

        if (amountString.isEmpty()) {
            tilAmount.error = "Tienes que ingresar un monto."
            tilAmount.isErrorEnabled = true
            return
        }

        if (amountString.toFloatOrNull() == null) {
            tilAmount.error = "Tienes que ingresar un numero"
            tilAmount.isErrorEnabled = true
            return
        }

        val detail = tiDetail.text.toString()

        if (detail.isEmpty()) {
            tilDetail.isErrorEnabled = true
            return
        }

        ownerUid?.let { id -> mPresenter?.createExpenditure(id, debtors, detail, amountString.toFloat()) }
    }

    override fun onExpenditureCreated() {
        onBackPressed()
    }

    override fun onError(e: String) {
        Log.e(this.localClassName, e)
    }

    override fun onCompanionsObtained(companions: ArrayList<CompanionModel>) {
        val namesList = ArrayList<SpecialPair>()
        namesList.add(SpecialPair("", "Select a companion"))

        companions.forEach { companion ->
            createCompanionCheckBox(companion)

            namesList.add(SpecialPair(companion.uid, companion.name))
        }

        loadSpinnerData(namesList)
        //Tal vez se debe implementar un loading.
    }

    class SpecialPair(val first: String, private val second: String) {
        override fun toString(): String {
            return second
        }
    }
}