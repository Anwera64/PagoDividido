package com.anwera64.pagodividido.presentation.newtrip

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.children
import androidx.lifecycle.Observer
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.data.entities.Trip
import com.anwera64.pagodividido.databinding.ActivityNewTripBinding
import com.anwera64.pagodividido.presentation.base.BaseViewModelActivity
import com.anwera64.pagodividido.presentation.trip.TripActivity
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
        if (name.isEmpty() || name.isBlank()) {
            tilName.isErrorEnabled = true
            return
        }

        val companions = cpCompanions.children.mapNotNull { view ->
            if (view is Chip) view.text.toString() else null
        }.toList()
        if (companions.isEmpty() || companions.size < 2) {
            tilCompanion.isErrorEnabled = true
            tilCompanion.error = getString(R.string.input_error_no_companions)
            return
        }
        tilCompanion.isErrorEnabled = false

        viewModel.createTrip(companions, name)
    }

    private fun observeCreatedTrip(trip: Trip?) {
        trip?.run { onTripCreated(id, name) } ?: onTripFailed()
    }

    private fun onTripCreated(uid: Int, name: String) {
        Intent(this, TripActivity::class.java).run {
            putExtra(TripActivity.TRIP_ID, uid.toString())
            putExtra(TripActivity.NAME, name)
            startActivity(this)
        }
    }

    private fun onTripFailed() {
        Log.e("New Trip", "We failed to create the trip")
    }

    override fun setupObservers() {
        viewModel.createdTrip.observe(this, Observer(::observeCreatedTrip))
    }
}