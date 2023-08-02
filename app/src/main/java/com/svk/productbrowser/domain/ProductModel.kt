package com.svk.productbrowser.domain

/**
 * Application use-case data model
 */
data class ProductModel(val id: Int, val title:String, val thumbnail:String,
                        val description:String, val price:Int)