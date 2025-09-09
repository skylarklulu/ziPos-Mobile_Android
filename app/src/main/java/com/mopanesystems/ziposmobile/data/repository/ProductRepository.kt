package com.mopanesystems.ziposmobile.data.repository

import com.mopanesystems.ziposmobile.data.database.dao.ProductDao
import com.mopanesystems.ziposmobile.data.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {
    fun getAllActiveProducts(): Flow<List<Product>> = productDao.getAllActiveProducts()
    
    fun getProductsByStore(storeId: String): Flow<List<Product>> = productDao.getProductsByStore(storeId)
    
    suspend fun getProductById(id: String): Product? = productDao.getProductById(id)
    
    suspend fun getProductByBarcode(barcode: String): Product? = productDao.getProductByBarcode(barcode)
    
    suspend fun getProductBySku(sku: String): Product? = productDao.getProductBySku(sku)
    
    fun searchProducts(searchQuery: String): Flow<List<Product>> = productDao.searchProducts("%$searchQuery%")
    
    fun getProductsByCategory(categoryId: String): Flow<List<Product>> = productDao.getProductsByCategory(categoryId)
    
    fun getLowStockProducts(): Flow<List<Product>> = productDao.getLowStockProducts()
    
    fun getOutOfStockProducts(): Flow<List<Product>> = productDao.getOutOfStockProducts()
    
    suspend fun insertProduct(product: Product) = productDao.insertProduct(product)
    
    suspend fun insertProducts(products: List<Product>) = productDao.insertProducts(products)
    
    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)
    
    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)
    
    suspend fun updateStockQuantity(productId: String, quantity: Int) = productDao.updateStockQuantity(productId, quantity)
    
    suspend fun deactivateProduct(productId: String) = productDao.deactivateProduct(productId)
    
    suspend fun getActiveProductCount(): Int = productDao.getActiveProductCount()
    
    suspend fun getLowStockCount(): Int = productDao.getLowStockCount()
}
