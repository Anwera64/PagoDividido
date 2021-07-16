package com.anwera64.pagodividido.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @BindingAdapter("app:srcCompat")
    @JvmStatic
    fun bindImageDrawable(view: ImageView, drawable: Drawable) {
        view.setImageDrawable(drawable)
    }
}