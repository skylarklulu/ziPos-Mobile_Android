package com.mopanesystems.ziposmobile.data.database.dao

import androidx.room.*
import com.mopanesystems.ziposmobile.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE storeId = :storeId AND isActive = 1 ORDER BY name ASC")
    fun getProductsByStore(storeId: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: String): Product?

    @Query("SELECT * FROM products WHERE barcode = :barcode AND isActive = 1")
    suspend fun getProductByBarcode(barcode: String): Product?

    @Query("SELECT * FROM products WHERE sku = :sku AND isActive = 1")
    suspend fun getProductBySku(sku: String): Product?

    @Query("SELECT * FROM products WHERE name LIKE :searchQuery AND isActive = 1 ORDER BY name ASC")
    fun searchProducts(searchQuery: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId AND isActive = 1 ORDER BY name ASC")
    fun getProductsByCategory(categoryId: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE stockQuantity <= minStockLevel AND isActive = 1 ORDER BY stockQuantity ASC")
    fun getLowStockProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE stockQuantity = 0 AND isActive = 1 ORDER BY name ASC")
    fun getOutOfStockProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("UPDATE products SET stockQuantity = :quantity WHERE id = :productId")
    suspend fun updateStockQuantity(productId: String, quantity: Int)

    @Query("UPDATE products SET isActive = 0 WHERE id = :productId")
    suspend fun deactivateProduct(productId: String)

    @Query("SELECT COUNT(*) FROM products WHERE isActive = 1")
    suspend fun getActiveProductCount(): Int

    @Query("SELECT COUNT(*) FROM products WHERE stockQuantity <= minStockLevel AND isActive = 1")
    suspend fun getLowStockCount(): Int
}
