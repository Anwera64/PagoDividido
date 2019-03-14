package com.anwera64.pagodividido.utils

import android.content.res.Resources

class ViewUtils {

    companion object {
        fun gerMarginInDP(dpValue: Int, resources: Resources): Int {
            val d = resources.displayMetrics.density

            return (dpValue * d).toInt()
        }
    }
}