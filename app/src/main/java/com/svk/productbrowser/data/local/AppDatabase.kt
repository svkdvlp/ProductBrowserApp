package com.svk.productbrowser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * AppDatabase : For local database
 */
@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}