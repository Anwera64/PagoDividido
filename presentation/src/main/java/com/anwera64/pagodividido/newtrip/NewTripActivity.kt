package com.anwera64.pagodividido.newtrip

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.children
import androidx.lifecycle.Observer
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.base.BaseViewModelActivity
import com.anwera64.pagodividido.databinding.ActivityNewTripBinding
import com.anwera64.pagodividido.databinding.CreateTripLayoutBinding
import com.anwera64.pagodividido.trip.TripActivity
import com.anwera64.pagodividido.utils.EventWrapper
import com.anwera64.pagodividido.utils.nullOrHandled
import com.anwera97.domain.models.TripShortModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class NewTripActivity : BaseViewModelActivity<NewTripViewModel, ActivityNewTripBinding>(NewTripViewModel::class) {

    override val layout: Int = R.layout.activity_new_trip
    override val viewModelValue: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.inInputFields.btnNewCompanion.setOnClickListener { addNewCompanionTextBox() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_create, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create -> prepareToCreate()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createCompanionChip(name: String): Chip {
        return Chip(this).apply {
            text = name
            isCloseIconVisible = true
            ellipsize = TextUtils.TruncateAt.END
            setOnCloseIconClickListener { (parent as? ChipGroup)?.removeView(this) }
        }
    }

    private fun addNewCompanionTextBox() = with(binding.inInputFields) {
        if (tiCompanion.text.isNullOrEmpty()) {
            tilCompanion.error = getString(R.string.input_error_companion_name)
            tilCompanion.isErrorEnabled = true
            return
        }
        tilCompanion.isErrorEnabled = false
        cpCompanions.addView(createCompanionChip(tiCompanion.text.toString().trim()))
        tiCompanion.setText("")
    }

    private fun prepareToCreate() = with(binding.inInputFields) {
        val name = tiName.text.toString().trim()
        val companions = cpCompanions.children.mapNotNull(::getChipNames).toList()
        tilCompanion.isErrorEnabled = false
        viewModel.createTrip(companions, name)
    }

    private fun setNameFieldError(enabled: Boolean) = with(binding.inInputFields) {
        tilName.isErrorEnabled = enabled
        tilName.error = getString(R.string.error_field_required)
    }

    private fun getChipNames(view: View): String? {
        return if (view is Chip) view.text.toString() else null
    }

    private fun setCompanionsError(enabled: Boolean) = with(binding.inInputFields) {
        tilCompanion.isErrorEnabled = enabled
        tilCompanion.error = getString(R.string.input_error_no_companions)
    }

    private fun observeCreatedTrip(eventWrapper: EventWrapper<TripShortModel>?) {
        if (eventWrapper.nullOrHandled()) return
        val trip = eventWrapper.getContentIfHandled()
        trip?.run { onTripCreated(id, name) } ?: onTripFailed()
    }

    private fun onTripCreated(uid: Int, name: String) {
        Intent(this, TripActivity::class.java)
            .putExtra(TripActivity.TRIP_ID, uid.toString())
            .putExtra(TripActivity.NAME, name)
            .let(::startActivity)
    }

    private fun onTripFailed() {
        Log.e("New Trip", "We failed to create the trip")
    }

    override fun setupObservers() {
        viewModel.createdTrip.observe(this, Observer(::observeCreatedTrip))
        viewModel.companionLimitError.observe(this, Observer(::setCompanionsError))
        viewModel.nameError.observe(this, Observer(::setNameFieldError))
    }
}