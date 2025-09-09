package com.mopanesystems.ziposmobile.data.repository

import com.mopanesystems.ziposmobile.data.database.dao.*
import com.mopanesystems.ziposmobile.data.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRepository @Inject constructor(
    private val inventoryAdjustmentDao: InventoryAdjustmentDao,
    private val stockAlertDao: StockAlertDao,
    private val supplierDao: SupplierDao,
    private val purchaseOrderDao: PurchaseOrderDao,
    private val purchaseOrderItemDao: PurchaseOrderItemDao
) {
    // Inventory Adjustments
    fun getAllAdjustments(): Flow<List<InventoryAdjustment>> = inventoryAdjustmentDao.getAllAdjustments()
    
    fun getAdjustmentsByStore(storeId: String): Flow<List<InventoryAdjustment>> = inventoryAdjustmentDao.getAdjustmentsByStore(storeId)
    
    fun getAdjustmentsByProduct(productId: String): Flow<List<InventoryAdjustment>> = inventoryAdjustmentDao.getAdjustmentsByProduct(productId)
    
    fun getAdjustmentsByUser(userId: String): Flow<List<InventoryAdjustment>> = inventoryAdjustmentDao.getAdjustmentsByUser(userId)
    
    fun getAdjustmentsByReason(reason: AdjustmentReason): Flow<List<InventoryAdjustment>> = inventoryAdjustmentDao.getAdjustmentsByReason(reason.name)
    
    suspend fun insertAdjustment(adjustment: InventoryAdjustment) = inventoryAdjustmentDao.insertAdjustment(adjustment)
    
    suspend fun updateAdjustment(adjustment: InventoryAdjustment) = inventoryAdjustmentDao.updateAdjustment(adjustment)
    
    suspend fun deleteAdjustment(adjustment: InventoryAdjustment) = inventoryAdjustmentDao.deleteAdjustment(adjustment)
    
    // Stock Alerts
    fun getActiveAlerts(): Flow<List<StockAlert>> = stockAlertDao.getActiveAlerts()
    
    fun getActiveAlertsByStore(storeId: String): Flow<List<StockAlert>> = stockAlertDao.getActiveAlertsByStore(storeId)
    
    fun getActiveAlertsByProduct(productId: String): Flow<List<StockAlert>> = stockAlertDao.getActiveAlertsByProduct(productId)
    
    fun getAlertsByType(alertType: AlertType): Flow<List<StockAlert>> = stockAlertDao.getAlertsByType(alertType.name)
    
    suspend fun insertAlert(alert: StockAlert) = stockAlertDao.insertAlert(alert)
    
    suspend fun updateAlert(alert: StockAlert) = stockAlertDao.updateAlert(alert)
    
    suspend fun deleteAlert(alert: StockAlert) = stockAlertDao.deleteAlert(alert)
    
    suspend fun acknowledgeAlert(alertId: String, acknowledgedAt: LocalDateTime) = 
        stockAlertDao.acknowledgeAlert(alertId, acknowledgedAt)
    
    suspend fun deactivateAlertsForProduct(productId: String) = stockAlertDao.deactivateAlertsForProduct(productId)
    
    // Suppliers
    fun getAllActiveSuppliers(): Flow<List<Supplier>> = supplierDao.getAllActiveSuppliers()
    
    fun getSuppliersByStore(storeId: String): Flow<List<Supplier>> = supplierDao.getSuppliersByStore(storeId)
    
    suspend fun getSupplierById(id: String): Supplier? = supplierDao.getSupplierById(id)
    
    fun searchSuppliers(searchQuery: String): Flow<List<Supplier>> = supplierDao.searchSuppliers("%$searchQuery%")
    
    suspend fun insertSupplier(supplier: Supplier) = supplierDao.insertSupplier(supplier)
    
    suspend fun updateSupplier(supplier: Supplier) = supplierDao.updateSupplier(supplier)
    
    suspend fun deleteSupplier(supplier: Supplier) = supplierDao.deleteSupplier(supplier)
    
    suspend fun deactivateSupplier(supplierId: String) = supplierDao.deactivateSupplier(supplierId)
    
    // Purchase Orders
    fun getAllOrders(): Flow<List<PurchaseOrder>> = purchaseOrderDao.getAllOrders()
    
    fun getOrdersByStore(storeId: String): Flow<List<PurchaseOrder>> = purchaseOrderDao.getOrdersByStore(storeId)
    
    fun getOrdersBySupplier(supplierId: String): Flow<List<PurchaseOrder>> = purchaseOrderDao.getOrdersBySupplier(supplierId)
    
    fun getOrdersByStatus(status: OrderStatus): Flow<List<PurchaseOrder>> = purchaseOrderDao.getOrdersByStatus(status.name)
    
    suspend fun getOrderById(id: String): PurchaseOrder? = purchaseOrderDao.getOrderById(id)
    
    suspend fun insertOrder(order: PurchaseOrder) = purchaseOrderDao.insertOrder(order)
    
    suspend fun updateOrder(order: PurchaseOrder) = purchaseOrderDao.updateOrder(order)
    
    suspend fun deleteOrder(order: PurchaseOrder) = purchaseOrderDao.deleteOrder(order)
    
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) = purchaseOrderDao.updateOrderStatus(orderId, status.name)
    
    // Purchase Order Items
    fun getOrderItems(orderId: String): Flow<List<PurchaseOrderItem>> = purchaseOrderItemDao.getOrderItems(orderId)
    
    suspend fun insertOrderItem(item: PurchaseOrderItem) = purchaseOrderItemDao.insertOrderItem(item)
    
    suspend fun insertOrderItems(items: List<PurchaseOrderItem>) = purchaseOrderItemDao.insertOrderItems(items)
    
    suspend fun updateOrderItem(item: PurchaseOrderItem) = purchaseOrderItemDao.updateOrderItem(item)
    
    suspend fun deleteOrderItem(item: PurchaseOrderItem) = purchaseOrderItemDao.deleteOrderItem(item)
    
    suspend fun deleteOrderItems(orderId: String) = purchaseOrderItemDao.deleteOrderItems(orderId)
}
