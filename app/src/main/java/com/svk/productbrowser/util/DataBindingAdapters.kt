package com.svk.productbrowser.util

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import coil.load
import com.svk.productbrowser.domain.ProductDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

@BindingAdapter("android:src")
fun ImageView.setImageUri(imageUri: String?) {
    if (imageUri == null) {
        this.setImageURI(null)
    } else {
        this.load(imageUri)
    }
}

@BindingAdapter("productTitle")
fun TextView.productTitle(detail:StateFlow<ProductDetailState>) {
    val responseState = detail.value
    if(responseState is ProductDetailState.Error)
        text = responseState.error
    else if(responseState is ProductDetailState.Success)
        text = responseState.product.title
}

@BindingAdapter("productDesc")
fun TextView.productDesc(detail:StateFlow<ProductDetailState>) {
    val responseState = detail.value
    if(responseState is ProductDetailState.Error)
        text = responseState.error
    else if(responseState is ProductDetailState.Success)
        text = responseState.product.description
}

@SuppressLint("SetTextI18n")
@BindingAdapter("productPrice")
fun TextView.productPrice(detail:StateFlow<ProductDetailState>) {
    val responseState = detail.value
    if(responseState is ProductDetailState.Error)
        text = responseState.error
    else if(responseState is ProductDetailState.Success)
        text = "Price: ${responseState.product.price}"
}

@BindingAdapter("productImageUrl")
fun ImageView.productImageUrl(detail:StateFlow<ProductDetailState>) {
    val responseState = detail.value
    if(responseState is ProductDetailState.Success)
        this.load(responseState.product.thumbnail)
    else
        this.setImageURI(null)
}
