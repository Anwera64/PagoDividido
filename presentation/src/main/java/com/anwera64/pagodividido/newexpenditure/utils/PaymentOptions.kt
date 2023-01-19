package com.anwera64.pagodividido.newexpenditure.utils

import androidx.annotation.StringRes
import com.anwera64.pagodividido.R

enum class PaymentOptions(@StringRes val stringRes: Int) {
    EQUALS(R.string.equals), DETAILED_AMOUNTS(R.string.detailed_amounts)
}