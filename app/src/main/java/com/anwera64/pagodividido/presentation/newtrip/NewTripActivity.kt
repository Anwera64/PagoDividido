package com.anwera64.pagodividido.presentation.newtrip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.data.entities.Trip
import com.anwera64.pagodividido.databinding.ActivityNewTripBinding
import com.anwera64.pagodividido.presentation.PagoDividioApp
import com.anwera64.pagodividido.presentation.base.BaseActivity
import com.anwera64.pagodividido.presentation.trip.TripActivity
import com.anwera64.pagodividido.utils.ViewUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class NewTripActivity : BaseActivity<ActivityNewTripBinding>() {

    companion object {
        const val TEXT_INPUT_TAG = "tiCompanion"
    }

    override val layout: Int = R.layout.activity_new_trip
    private val viewModel: NewTripViewModel by viewModels {
        val app = (application as PagoDividioApp)
        NewTripViewModelFactory(app.tripRepository, app.companionRepository)
    }
    private var count = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        viewModel.createdTrip.observe(this, Observer(::observeCreatedTrip))
    }

    private fun setupUi() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnNewCompanion.setOnClickListener { addNewCompanionTextBox() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create -> prepareToCreate()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addNewCompanionTextBox() = with(binding) {
        val textInputLayout = createTextInputLayout()
        val textInputEditText = createTextInputEditText()

        textInputLayout.addView(textInputEditText)
        llCompanions.addView(textInputLayout, llCompanions.childCount - 1)

        textInputEditText.requestFocus()
        count++
    }

    private fun createTextInputLayout(): TextInputLayout {
        val textInputLayout = TextInputLayout(this)
        val tilParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val margin = ViewUtils.gerMarginInDP(16, resources)

        tilParams.setMargins(margin, margin, margin, margin)
        textInputLayout.layoutParams = tilParams
        textInputLayout.tag = "$TEXT_INPUT_TAG${count + 1}"

        return textInputLayout
    }

    private fun createTextInputEditText(): TextInputEditText {
        val textInputEditText = TextInputEditText(this)
        val tiParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textInputEditText.layoutParams = tiParams
        textInputEditText.hint =
            resources.getString(R.string.companion_name)

        return textInputEditText
    }

    private fun prepareToCreate() = with(binding) {
        val name = tiName.text.toString()
        if (name.isEmpty() || name.isBlank()) {
            tilName.isErrorEnabled = true
            return
        }

        val companions = arrayListOf<String>()
        for (i in 1..count) {
            val textInputLayout = llCompanions.findViewWithTag<TextInputLayout>("$TEXT_INPUT_TAG$i")

            val textInputEditText = (textInputLayout.getChildAt(0) as ViewGroup)
                .getChildAt(0) as TextInputEditText

            val companionName = textInputEditText.text.toString()
            if (companionName.isEmpty() || companionName.isBlank()) {
                textInputLayout.isErrorEnabled = true
                return
            }
            companions.add(companionName)
        }

        viewModel.createTrip(companions, name)
    }

    private fun observeCreatedTrip(trip: Trip?) {
        trip?.run { onTripCreated(id, name) } ?: onTripFailed()
    }

    private fun onTripCreated(uid: Int, name: String) {
        Intent(this, TripActivity::class.java).run {
            putExtra(TripActivity.TRIP_ID, uid)
            putExtra(TripActivity.NAME, name)
            startActivity(this)
        }
    }

    private fun onTripFailed() {
        Log.e("New Trip", "We failed to create the trip")
    }
}