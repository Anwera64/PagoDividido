package com.anwera64.pagodividido.utils

import android.content.res.Resources
import android.util.TypedValue

class ViewUtils {

    companion object {
        fun gerMarginInDP(dpValue: Float, resources: Resources): Int {
            return TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.displayMetrics
            ).toInt()
        }
    }
}