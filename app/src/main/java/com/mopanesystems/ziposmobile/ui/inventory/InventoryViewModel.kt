package com.mopanesystems.ziposmobile.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mopanesystems.ziposmobile.data.repository.InventoryRepository
import com.mopanesystems.ziposmobile.data.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {

    private val _stockAlerts = MutableLiveData<List<StockAlert>>()
    val stockAlerts: LiveData<List<StockAlert>> = _stockAlerts

    private val _recentAdjustments = MutableLiveData<List<InventoryAdjustment>>()
    val recentAdjustments: LiveData<List<InventoryAdjustment>> = _recentAdjustments

    private val _lowStockCount = MutableLiveData<Int>()
    val lowStockCount: LiveData<Int> = _lowStockCount

    private val _outOfStockCount = MutableLiveData<Int>()
    val outOfStockCount: LiveData<Int> = _outOfStockCount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        loadInventoryData()
    }

    private fun loadInventoryData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Load stock alerts
                val alerts = inventoryRepository.getActiveAlerts()
                alerts.collect { alertList ->
                    _stockAlerts.value = alertList
                    updateStockCounts(alertList)
                }
                
                // Load recent adjustments (last 10)
                val adjustments = inventoryRepository.getAllAdjustments()
                adjustments.collect { adjustmentList ->
                    _recentAdjustments.value = adjustmentList.take(10)
                }
                
            } catch (e: Exception) {
                _errorMessage.value = "Error loading inventory data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun updateStockCounts(alerts: List<StockAlert>) {
        val lowStock = alerts.count { it.alertType == StockAlertType.LOW_STOCK }
        val outOfStock = alerts.count { it.alertType == StockAlertType.OUT_OF_STOCK }
        
        _lowStockCount.value = lowStock
        _outOfStockCount.value = outOfStock
    }

    fun acknowledgeAlert(alert: StockAlert) {
        viewModelScope.launch {
            try {
                inventoryRepository.acknowledgeAlert(alert.id, LocalDateTime.now())
                loadInventoryData() // Refresh data
            } catch (e: Exception) {
                _errorMessage.value = "Error acknowledging alert: ${e.message}"
            }
        }
    }

    fun createStockAdjustment(
        productId: String,
        quantity: Int,
        reason: AdjustmentReason,
        notes: String?,
        adjustedBy: String,
        storeId: String
    ) {
        viewModelScope.launch {
            try {
                val adjustment = InventoryAdjustment(
                    id = UUID.randomUUID().toString(),
                    productId = productId,
                    quantity = quantity,
                    reason = reason,
                    notes = notes,
                    userId = adjustedBy,
                    storeId = storeId,
                    createdAt = LocalDateTime.now()
                )
                
                inventoryRepository.insertAdjustment(adjustment)
                
                // Update product stock
                updateProductStock(productId, quantity)
                
                // Check for new stock alerts
                checkStockAlerts(productId)
                
                loadInventoryData() // Refresh data
                
            } catch (e: Exception) {
                _errorMessage.value = "Error creating stock adjustment: ${e.message}"
            }
        }
    }

    private suspend fun updateProductStock(productId: String, quantityChange: Int) {
        // This would typically update the product's stock quantity
        // For now, we'll just log it
        // TODO: Implement actual stock update logic
    }

    private suspend fun checkStockAlerts(productId: String) {
        // This would check if the product needs stock alerts
        // For now, we'll just log it
        // TODO: Implement stock alert checking logic
    }

    fun createStockAlert(
        productId: String,
        currentStock: Int,
        minThreshold: Int,
        alertType: StockAlertType,
        storeId: String
    ) {
        viewModelScope.launch {
            try {
                val alert = StockAlert(
                    id = UUID.randomUUID().toString(),
                    productId = productId,
                    currentStock = currentStock,
                    threshold = minThreshold,
                    alertType = alertType,
                    isActive = true,
                    createdAt = LocalDateTime.now(),
                    storeId = storeId
                )
                
                inventoryRepository.insertAlert(alert)
                loadInventoryData() // Refresh data
                
            } catch (e: Exception) {
                _errorMessage.value = "Error creating stock alert: ${e.message}"
            }
        }
    }

    fun refreshData() {
        loadInventoryData()
    }
}
