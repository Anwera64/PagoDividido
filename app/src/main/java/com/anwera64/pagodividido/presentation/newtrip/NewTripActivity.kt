package com.anwera64.pagodividido.presentation.newtrip

import android.content.Intent
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.domain.models.Companion
import com.anwera64.pagodividido.presentation.trip.TripActivity
import com.anwera64.pagodividido.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_new_trip.*
import java.util.*


class NewTripActivity : AppCompatActivity(), NewTripActivityPresenter.NewTripActivityDelegate {

    private val mPresenter = NewTripActivityPresenter(this)
    private var count = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnNewCompanion.setOnClickListener { addNewCompanionTextBox() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.create -> prepareToCreate()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addNewCompanionTextBox() {
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
        textInputLayout.tag = "tiCompanion${count + 1}"

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

    private fun prepareToCreate() {
        val name = tiName.text.toString()
        if (name.isEmpty() || name.isBlank()) {
            tilName.isErrorEnabled = true
            return
        }

        val companions = HashMap<String, Companion>()
        for (i in 1..count) {
            val textInputLayout = llCompanions.findViewWithTag<TextInputLayout>("tiCompanion$i")

            val textInputEditText = (textInputLayout.getChildAt(0) as ViewGroup)
                .getChildAt(0) as TextInputEditText

            val companionName = textInputEditText.text.toString()
            if (companionName.isEmpty() || companionName.isBlank()) {
                textInputLayout.isErrorEnabled = true
                return
            }

            val uid = UUID.randomUUID().toString()
            companions[uid] = Companion(uid, companionName, 0.0f, 0.0f)
        }

        mPresenter.createTrip(companions, name)
    }

    override fun onTripCreated(uid: String, name: String) {
        val intent = Intent(this, TripActivity::class.java)
        intent.putExtra("tripUid", uid)
        intent.putExtra("name", name)
        startActivity(intent)
    }

    override fun onTripFailed() {
        Log.e("New Trip", "We failed to create the trip")
    }
}