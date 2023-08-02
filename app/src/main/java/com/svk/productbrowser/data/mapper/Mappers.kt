package com.svk.productbrowser.data.mapper

import com.svk.productbrowser.data.local.ProductEntity
import com.svk.productbrowser.data.remote.models.Product
import com.svk.productbrowser.domain.ProductModel

/**
 * Product to ProductModel mapper
 */
fun Product.toProductModel(): ProductModel {
    return ProductModel(id = id, title= title, thumbnail = thumbnail,
        description = description, price = price)
}


/**
 * Product to ProductEntity mapper
 */
fun Product.toProductEntity(): ProductEntity {
    return ProductEntity(id = id,
        title= title,
        thumbnail = thumbnail,
        description = description,
        price = price,
        time = System.currentTimeMillis())
}

/**
 * ProductEntity to ProductModel mapper
 */
fun ProductEntity.toProductModel(): ProductModel {
    return ProductModel(id = id,
        title= title,
        thumbnail = thumbnail,
        description = description,
        price = price)
}