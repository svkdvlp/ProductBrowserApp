package com.svk.productbrowser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ProductEntity : Table schema for storing data
 */
@Entity(tableName = "products_table")
data class ProductEntity(
        @PrimaryKey val id: Int,
        val title:String,
        val thumbnail:String,
        val description:String,
        val price:Int,
        val time: Long,
)