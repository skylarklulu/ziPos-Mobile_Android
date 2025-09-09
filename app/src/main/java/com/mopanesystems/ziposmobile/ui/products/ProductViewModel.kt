package com.mopanesystems.ziposmobile.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mopanesystems.ziposmobile.data.model.Product
import com.mopanesystems.ziposmobile.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())
    val filteredProducts: StateFlow<List<Product>> = _filteredProducts.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                productRepository.getAllActiveProducts().collect { productList ->
                    _products.value = productList
                    applyFilters()
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchProducts(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun filterByCategory(categoryId: String?) {
        _selectedCategory.value = categoryId
        applyFilters()
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
        applyFilters()
    }

    private fun applyFilters() {
        val query = _searchQuery.value.lowercase()
        val categoryId = _selectedCategory.value

        val filtered = _products.value.filter { product ->
            val matchesSearch = query.isEmpty() || 
                product.name.lowercase().contains(query) ||
                product.sku.lowercase().contains(query) ||
                product.barcode?.lowercase()?.contains(query) == true

            val matchesCategory = categoryId == null || product.categoryId == categoryId

            matchesSearch && matchesCategory
        }

        _filteredProducts.value = filtered
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.insertProduct(product)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            try {
                val updatedProduct = product.copy(updatedAt = LocalDateTime.now())
                productRepository.updateProduct(updatedProduct)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.deactivateProduct(product.id)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun updateStockQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            try {
                productRepository.updateStockQuantity(productId, quantity)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun getProductByBarcode(barcode: String) {
        viewModelScope.launch {
            try {
                val product = productRepository.getProductByBarcode(barcode)
                if (product != null) {
                    // Handle found product (e.g., add to cart in POS)
                    _errorMessage.value = "Product found: ${product.name}"
                } else {
                    _errorMessage.value = "Product not found for barcode: $barcode"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun getLowStockProducts() {
        viewModelScope.launch {
            try {
                productRepository.getLowStockProducts().collect { lowStockProducts ->
                    _filteredProducts.value = lowStockProducts
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun getOutOfStockProducts() {
        viewModelScope.launch {
            try {
                productRepository.getOutOfStockProducts().collect { outOfStockProducts ->
                    _filteredProducts.value = outOfStockProducts
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Helper function to create a new product
    fun createNewProduct(
        name: String,
        sku: String,
        price: BigDecimal,
        categoryId: String? = null,
        barcode: String? = null,
        description: String? = null,
        cost: BigDecimal? = null,
        stockQuantity: Int = 0,
        minStockLevel: Int = 0,
        isTaxable: Boolean = true,
        taxRate: BigDecimal = BigDecimal.ZERO
    ): Product {
        return Product(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description,
            sku = sku,
            barcode = barcode,
            price = price,
            cost = cost,
            categoryId = categoryId,
            stockQuantity = stockQuantity,
            minStockLevel = minStockLevel,
            isActive = true,
            isTaxable = isTaxable,
            taxRate = taxRate,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}
