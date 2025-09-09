package com.mopanesystems.ziposmobile.data.database.dao

import androidx.room.*
import com.mopanesystems.ziposmobile.data.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface InventoryAdjustmentDao {
    @Query("SELECT * FROM inventory_adjustments ORDER BY createdAt DESC")
    fun getAllAdjustments(): Flow<List<InventoryAdjustment>>

    @Query("SELECT * FROM inventory_adjustments WHERE storeId = :storeId ORDER BY createdAt DESC")
    fun getAdjustmentsByStore(storeId: String): Flow<List<InventoryAdjustment>>

    @Query("SELECT * FROM inventory_adjustments WHERE productId = :productId ORDER BY createdAt DESC")
    fun getAdjustmentsByProduct(productId: String): Flow<List<InventoryAdjustment>>

    @Query("SELECT * FROM inventory_adjustments WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAdjustmentsByUser(userId: String): Flow<List<InventoryAdjustment>>

    @Query("SELECT * FROM inventory_adjustments WHERE reason = :reason ORDER BY createdAt DESC")
    fun getAdjustmentsByReason(reason: String): Flow<List<InventoryAdjustment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdjustment(adjustment: InventoryAdjustment)

    @Update
    suspend fun updateAdjustment(adjustment: InventoryAdjustment)

    @Delete
    suspend fun deleteAdjustment(adjustment: InventoryAdjustment)
}

@Dao
interface StockAlertDao {
    @Query("SELECT * FROM stock_alerts WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getActiveAlerts(): Flow<List<StockAlert>>

    @Query("SELECT * FROM stock_alerts WHERE storeId = :storeId AND isActive = 1 ORDER BY createdAt DESC")
    fun getActiveAlertsByStore(storeId: String): Flow<List<StockAlert>>

    @Query("SELECT * FROM stock_alerts WHERE productId = :productId AND isActive = 1")
    fun getActiveAlertsByProduct(productId: String): Flow<List<StockAlert>>

    @Query("SELECT * FROM stock_alerts WHERE alertType = :alertType AND isActive = 1 ORDER BY createdAt DESC")
    fun getAlertsByType(alertType: String): Flow<List<StockAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: StockAlert)

    @Update
    suspend fun updateAlert(alert: StockAlert)

    @Delete
    suspend fun deleteAlert(alert: StockAlert)

    @Query("UPDATE stock_alerts SET isActive = 0, resolvedAt = :resolvedAt WHERE id = :alertId")
    suspend fun acknowledgeAlert(alertId: String, resolvedAt: LocalDateTime)

    @Query("UPDATE stock_alerts SET isActive = 0 WHERE productId = :productId")
    suspend fun deactivateAlertsForProduct(productId: String)
}

@Dao
interface SupplierDao {
    @Query("SELECT * FROM suppliers WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveSuppliers(): Flow<List<Supplier>>

    @Query("SELECT * FROM suppliers WHERE storeId = :storeId AND isActive = 1 ORDER BY name ASC")
    fun getSuppliersByStore(storeId: String): Flow<List<Supplier>>

    @Query("SELECT * FROM suppliers WHERE id = :id")
    suspend fun getSupplierById(id: String): Supplier?

    @Query("SELECT * FROM suppliers WHERE name LIKE :searchQuery AND isActive = 1 ORDER BY name ASC")
    fun searchSuppliers(searchQuery: String): Flow<List<Supplier>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplier(supplier: Supplier)

    @Update
    suspend fun updateSupplier(supplier: Supplier)

    @Delete
    suspend fun deleteSupplier(supplier: Supplier)

    @Query("UPDATE suppliers SET isActive = 0 WHERE id = :supplierId")
    suspend fun deactivateSupplier(supplierId: String)
}

@Dao
interface PurchaseOrderDao {
    @Query("SELECT * FROM purchase_orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<PurchaseOrder>>

    @Query("SELECT * FROM purchase_orders WHERE storeId = :storeId ORDER BY createdAt DESC")
    fun getOrdersByStore(storeId: String): Flow<List<PurchaseOrder>>

    @Query("SELECT * FROM purchase_orders WHERE supplierId = :supplierId ORDER BY createdAt DESC")
    fun getOrdersBySupplier(supplierId: String): Flow<List<PurchaseOrder>>

    @Query("SELECT * FROM purchase_orders WHERE status = :status ORDER BY createdAt DESC")
    fun getOrdersByStatus(status: String): Flow<List<PurchaseOrder>>

    @Query("SELECT * FROM purchase_orders WHERE id = :id")
    suspend fun getOrderById(id: String): PurchaseOrder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: PurchaseOrder)

    @Update
    suspend fun updateOrder(order: PurchaseOrder)

    @Delete
    suspend fun deleteOrder(order: PurchaseOrder)

    @Query("UPDATE purchase_orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)
}

@Dao
interface PurchaseOrderItemDao {
    @Query("SELECT * FROM purchase_order_items WHERE purchaseOrderId = :orderId")
    fun getOrderItems(orderId: String): Flow<List<PurchaseOrderItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(item: PurchaseOrderItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<PurchaseOrderItem>)

    @Update
    suspend fun updateOrderItem(item: PurchaseOrderItem)

    @Delete
    suspend fun deleteOrderItem(item: PurchaseOrderItem)

    @Query("DELETE FROM purchase_order_items WHERE purchaseOrderId = :orderId")
    suspend fun deleteOrderItems(orderId: String)

    // Offline Support
    @Query("SELECT * FROM inventory_adjustments WHERE isSynced = 0 ORDER BY createdAt ASC")
    fun getQueuedAdjustments(): Flow<List<InventoryAdjustment>>

    @Query("SELECT * FROM inventory_adjustments WHERE isSynced = 0 ORDER BY createdAt ASC")
    fun getUnsyncedAdjustments(): Flow<List<InventoryAdjustment>>

    @Query("UPDATE inventory_adjustments SET isSynced = 1 WHERE id = :adjustmentId")
    suspend fun markAdjustmentAsSynced(adjustmentId: String)
}
