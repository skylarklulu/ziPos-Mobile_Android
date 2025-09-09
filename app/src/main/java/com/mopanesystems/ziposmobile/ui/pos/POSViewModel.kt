package com.mopanesystems.ziposmobile.ui.pos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.mopanesystems.ziposmobile.data.model.*
import com.mopanesystems.ziposmobile.data.repository.CustomerRepository
import com.mopanesystems.ziposmobile.data.repository.ProductRepository
import com.mopanesystems.ziposmobile.data.repository.TransactionRepository
import com.mopanesystems.ziposmobile.services.ReceiptService
import com.mopanesystems.ziposmobile.services.ReceiptResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class POSViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val customerRepository: CustomerRepository,
    private val transactionRepository: TransactionRepository,
    private val context: Context
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _selectedCustomer = MutableStateFlow<Customer?>(null)
    val selectedCustomer: StateFlow<Customer?> = _selectedCustomer.asStateFlow()

    private val _transactionStatus = MutableStateFlow(TransactionStatus.PENDING)
    val transactionStatus: StateFlow<TransactionStatus> = _transactionStatus.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _currentTransaction = MutableLiveData<Transaction?>()
    val currentTransaction: LiveData<Transaction?> = _currentTransaction

    // Cart calculations
    val subtotal: StateFlow<BigDecimal>
        get() = MutableStateFlow(_cartItems.value.sumOf { it.totalPrice })

    val taxAmount: StateFlow<BigDecimal>
        get() = MutableStateFlow(_cartItems.value.sumOf { it.taxAmount })

    val totalAmount: StateFlow<BigDecimal>
        get() = MutableStateFlow(_cartItems.value.sumOf { it.totalPrice + it.taxAmount })

    val itemCount: StateFlow<Int>
        get() = MutableStateFlow(_cartItems.value.sumOf { it.quantity })

    fun addProductToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            val existingItemIndex = currentItems.indexOfFirst { it.product.id == product.id }

            if (existingItemIndex != -1) {
                // Update existing item
                val existingItem = currentItems[existingItemIndex]
                val newQuantity = existingItem.quantity + quantity
                currentItems[existingItemIndex] = existingItem.copy(
                    quantity = newQuantity,
                    totalPrice = product.price.multiply(BigDecimal(newQuantity)),
                    taxAmount = calculateTax(product.price.multiply(BigDecimal(newQuantity)), product.taxRate)
                )
            } else {
                // Add new item
                val totalPrice = product.price.multiply(BigDecimal(quantity))
                val taxAmount = calculateTax(totalPrice, product.taxRate)
                val newItem = CartItem(
                    id = UUID.randomUUID().toString(),
                    product = product,
                    quantity = quantity,
                    unitPrice = product.price,
                    totalPrice = totalPrice,
                    taxAmount = taxAmount
                )
                currentItems.add(newItem)
            }

            _cartItems.value = currentItems
            updateStockQuantity(product.id, -quantity)
        }
    }

    fun removeProductFromCart(productId: String) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            val itemToRemove = currentItems.find { it.product.id == productId }
            
            if (itemToRemove != null) {
                currentItems.remove(itemToRemove)
                _cartItems.value = currentItems
                updateStockQuantity(productId, itemToRemove.quantity)
            }
        }
    }

    fun updateCartItemQuantity(productId: String, newQuantity: Int) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            val itemIndex = currentItems.indexOfFirst { it.product.id == productId }

            if (itemIndex != -1) {
                val item = currentItems[itemIndex]
                val quantityDifference = newQuantity - item.quantity
                
                if (newQuantity <= 0) {
                    currentItems.removeAt(itemIndex)
                } else {
                    val totalPrice = item.unitPrice.multiply(BigDecimal(newQuantity))
                    val taxAmount = calculateTax(totalPrice, item.product.taxRate)
                    currentItems[itemIndex] = item.copy(
                        quantity = newQuantity,
                        totalPrice = totalPrice,
                        taxAmount = taxAmount
                    )
                }

                _cartItems.value = currentItems
                updateStockQuantity(productId, -quantityDifference)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            // Restore stock quantities
            _cartItems.value.forEach { item ->
                updateStockQuantity(item.product.id, item.quantity)
            }
            _cartItems.value = emptyList()
            _selectedCustomer.value = null
            _transactionStatus.value = TransactionStatus.PENDING
        }
    }

    fun selectCustomer(customer: Customer?) {
        _selectedCustomer.value = customer
    }

    fun findProductByBarcode(barcode: String) {
        viewModelScope.launch {
            try {
                val product = productRepository.getProductByBarcode(barcode)
                if (product != null) {
                    addProductToCart(product, 1)
                } else {
                    // TODO: Show error message - product not found
                }
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }

    fun processTransaction(paymentMethod: PaymentMethod, paidAmount: BigDecimal? = null) {
        viewModelScope.launch {
            if (_cartItems.value.isEmpty()) return

            _isProcessing.value = true
            _transactionStatus.value = TransactionStatus.IN_PROGRESS

            try {
                val transaction = createTransaction(paymentMethod, paidAmount)
                val transactionId = transaction.id

                // Insert transaction
                transactionRepository.insertTransaction(transaction)

                // Insert transaction items
                val transactionItems = _cartItems.value.map { cartItem ->
                    TransactionItem(
                        id = UUID.randomUUID().toString(),
                        transactionId = transactionId,
                        productId = cartItem.product.id,
                        quantity = cartItem.quantity,
                        unitPrice = cartItem.unitPrice,
                        totalPrice = cartItem.totalPrice,
                        taxAmount = cartItem.taxAmount
                    )
                }
                transactionRepository.insertTransactionItems(transactionItems)

                // Insert payment
                val payment = Payment(
                    id = UUID.randomUUID().toString(),
                    transactionId = transactionId,
                    amount = transaction.totalAmount,
                    method = paymentMethod,
                    status = PaymentStatus.COMPLETED,
                    processedAt = LocalDateTime.now()
                )
                transactionRepository.insertPayment(payment)

                // Update customer if selected
                _selectedCustomer.value?.let { customer ->
                    updateCustomerAfterTransaction(customer, transaction)
                }

                _transactionStatus.value = TransactionStatus.COMPLETED
                _currentTransaction.value = transaction

            } catch (e: Exception) {
                _transactionStatus.value = TransactionStatus.CANCELLED
                // Handle error
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun completeTransaction() {
        _transactionStatus.value = TransactionStatus.COMPLETED
        clearCart()
    }

    fun cancelTransaction() {
        viewModelScope.launch {
            // Restore stock quantities
            _cartItems.value.forEach { item ->
                updateStockQuantity(item.product.id, item.quantity)
            }
            clearCart()
        }
    }

    private suspend fun updateStockQuantity(productId: String, quantityChange: Int) {
        val product = productRepository.getProductById(productId)
        product?.let {
            val newQuantity = it.stockQuantity + quantityChange
            productRepository.updateStockQuantity(productId, newQuantity)
        }
    }

    private fun calculateTax(amount: BigDecimal, taxRate: BigDecimal): BigDecimal {
        return amount.multiply(taxRate).divide(BigDecimal(100))
    }

    private fun createTransaction(paymentMethod: PaymentMethod, paidAmount: BigDecimal?): Transaction {
        val subtotal = _cartItems.value.sumOf { it.totalPrice }
        val taxAmount = _cartItems.value.sumOf { it.taxAmount }
        val totalAmount = subtotal + taxAmount
        val paid = paidAmount ?: totalAmount
        val changeAmount = paid - totalAmount

        return Transaction(
            id = UUID.randomUUID().toString(),
            transactionNumber = generateTransactionNumber(),
            customerId = _selectedCustomer.value?.id,
            cashierId = "current_user_id", // TODO: Get from current user session
            storeId = "current_store_id", // TODO: Get from current store
            type = TransactionType.PURCHASE,
            status = TransactionStatus.PENDING,
            subtotal = subtotal,
            taxAmount = taxAmount,
            totalAmount = totalAmount,
            paidAmount = paid,
            changeAmount = changeAmount,
            paymentMethod = paymentMethod
        )
    }

    private fun updateCustomerAfterTransaction(customer: Customer, transaction: Transaction) {
        viewModelScope.launch {
            val updatedCustomer = customer.copy(
                totalSpent = customer.totalSpent + transaction.totalAmount,
                loyaltyPoints = customer.loyaltyPoints + calculateLoyaltyPoints(transaction.totalAmount)
            )
            customerRepository.updateCustomer(updatedCustomer)

            // Add customer transaction record
            val customerTransaction = CustomerTransaction(
                id = UUID.randomUUID().toString(),
                customerId = customer.id,
                transactionId = transaction.id,
                type = TransactionType.PURCHASE,
                amount = transaction.totalAmount,
                balance = updatedCustomer.balance
            )
            customerRepository.insertCustomerTransaction(customerTransaction)
        }
    }

    private fun calculateLoyaltyPoints(amount: BigDecimal): Int {
        // TODO: Get loyalty points per dollar from store settings
        return amount.multiply(BigDecimal(1)).toInt()
    }

    private fun generateTransactionNumber(): String {
        return "TXN-${System.currentTimeMillis()}"
    }

    fun generateReceipt(transaction: Transaction): ReceiptResult {
        return try {
            val receiptService = ReceiptService(context)
            val transactionItems = _cartItems.value.map { cartItem ->
                TransactionItem(
                    id = UUID.randomUUID().toString(),
                    transactionId = transaction.id,
                    productId = cartItem.product.id,
                    quantity = cartItem.quantity,
                    unitPrice = cartItem.unitPrice,
                    totalPrice = cartItem.totalPrice,
                    taxAmount = cartItem.taxAmount
                )
            }

            receiptService.generateReceipt(
                transaction = transaction,
                transactionItems = transactionItems,
                store = null, // TODO: Get current store
                customer = _selectedCustomer.value
            )
        } catch (e: Exception) {
            ReceiptResult.Error("Error generating receipt: ${e.message}")
        }
    }

    fun generateReceiptBitmap(transaction: Transaction): android.graphics.Bitmap? {
        val receiptService = ReceiptService(context)
        val transactionItems = _cartItems.value.map { cartItem ->
            TransactionItem(
                id = UUID.randomUUID().toString(),
                transactionId = transaction.id,
                productId = cartItem.product.id,
                quantity = cartItem.quantity,
                unitPrice = cartItem.unitPrice,
                totalPrice = cartItem.totalPrice,
                taxAmount = cartItem.taxAmount
            )
        }

        return receiptService.generateReceiptBitmap(
            transaction = transaction,
            transactionItems = transactionItems,
            store = null, // TODO: Get current store
            customer = _selectedCustomer.value
        )
    }
}

data class CartItem(
    val id: String,
    val product: Product,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal,
    val taxAmount: BigDecimal
)
