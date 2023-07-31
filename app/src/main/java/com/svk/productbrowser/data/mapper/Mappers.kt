package com.svk.productbrowser.data.mapper

import com.svk.productbrowser.data.remote.models.Product
import com.svk.productbrowser.domain.ProductModel

fun Product.toProductModel(): ProductModel {
    return ProductModel(
      id = id, title= title, thumbnail = thumbnail
    )
}