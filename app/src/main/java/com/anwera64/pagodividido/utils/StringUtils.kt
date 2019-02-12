package com.anwera64.pagodividido.utils

import android.text.Editable

class StringUtils {

    companion object {
        fun checkEmptyString(it: Editable): Boolean {
            return !it.toString().contentEquals("")
        }
    }
}