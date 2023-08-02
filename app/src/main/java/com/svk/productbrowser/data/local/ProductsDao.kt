package com.svk.productbrowser.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO interface for local db operations
 */
@Dao
interface ProductsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM products_table WHERE id=:product_id LIMIT 1")
    fun getProduct(product_id:Int): ProductEntity?

    @Query("SELECT * FROM products_table WHERE title LIKE '%' || :query || '%'")
    fun searchProducts(query:String): Flow<List<ProductEntity>>
}