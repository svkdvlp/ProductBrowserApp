package com.svk.productbrowser.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

object DataBindingAdapters {

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUri(view: ImageView, imageUri: String?) {
        if (imageUri == null) {
            view.setImageURI(null)
        } else {
            view.load(imageUri)
        }
    }
}
