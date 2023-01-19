package com.anwera64.pagodividido.newtrip

import androidx.annotation.StringRes
import com.anwera64.pagodividido.R

enum class NewTripErrorStates(@StringRes val errorResource: Int) {
    EMPTY_TITLE(R.string.error_field_required),
    NOT_ENOUGH_COMPANIONS(R.string.input_error_no_companions)
}