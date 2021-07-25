package com.anwera64.pagodividido.utils.uicomponents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.LayoutDebtorAmountInputBinding

class DebtorInputView(context: Context) : ConstraintLayout(context) {

    private val binding: LayoutDebtorAmountInputBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_debtor_amount_input, this, true)
        binding.cbCompanion.setOnCheckedChangeListener { _, isChecked ->
            isInputHidden = !isChecked
        }
    }

    var name: String
        set(value) {
            binding.cbCompanion.text = value
        }
        get() = binding.cbCompanion.text.toString()

    var isInputHidden: Boolean = true
        set(value) {
            field = value
            binding.tilAmountOwed.visibility = if (field) View.GONE else View.VISIBLE
        }

    fun isChecked(): Boolean = binding.cbCompanion.isChecked

    fun getAmount(): Double = binding.tiAmountOwed.text.toString().toDoubleOrNull() ?: 0.0

    fun setText(text: String) = binding.tiAmountOwed.setText(text)

    fun setText(@StringRes text: Int) = binding.tiAmountOwed.setText(text)

    fun setError(@StringRes text: Int) {
        binding.tilAmountOwed.error = context.getString(text)
    }

    fun setError(text: String) {
        binding.tilAmountOwed.error = text
    }

    fun setErrorEnabled(isEnabled: Boolean) {
        binding.tilAmountOwed.isErrorEnabled = isEnabled
    }
}