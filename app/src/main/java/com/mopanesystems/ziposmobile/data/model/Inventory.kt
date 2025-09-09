package com.mopanesystems.ziposmobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "inventory_adjustments")
data class InventoryAdjustment(
    @PrimaryKey
    val id: String,
    val productId: String,
    val quantity: Int,
    val reason: AdjustmentReason,
    val notes: String? = null,
    val userId: String,
    val storeId: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isSynced: Boolean = false
)

@Entity(tableName = "stock_alerts")
data class StockAlert(
    @PrimaryKey
    val id: String,
    val productId: String,
    val currentStock: Int,
    val threshold: Int,
    val alertType: StockAlertType,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val resolvedAt: LocalDateTime? = null,
    val acknowledgedAt: LocalDateTime? = null,
    val storeId: String
)

@Entity(tableName = "suppliers")
data class Supplier(
    @PrimaryKey
    val id: String,
    val name: String,
    val contactPerson: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zipCode: String? = null,
    val country: String? = null,
    val paymentTerms: String? = null,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val storeId: String? = null
)

@Entity(tableName = "purchase_orders")
data class PurchaseOrder(
    @PrimaryKey
    val id: String,
    val orderNumber: String,
    val supplierId: String,
    val status: OrderStatus,
    val totalAmount: BigDecimal,
    val notes: String? = null,
    val expectedDeliveryDate: LocalDateTime? = null,
    val actualDeliveryDate: LocalDateTime? = null,
    val createdBy: String,
    val storeId: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "purchase_order_items")
data class PurchaseOrderItem(
    @PrimaryKey
    val id: String,
    val purchaseOrderId: String,
    val productId: String,
    val quantity: Int,
    val unitCost: BigDecimal,
    val totalCost: BigDecimal,
    val receivedQuantity: Int = 0
)

enum class AdjustmentReason {
    STOCK_TAKE,
    DAMAGE,
    THEFT,
    RETURN,
    TRANSFER,
    CORRECTION,
    OTHER
}

enum class AlertType {
    LOW_STOCK,
    OUT_OF_STOCK,
    OVERSTOCK,
    EXPIRING_SOON
}

enum class StockAlertType {
    LOW_STOCK,
    OUT_OF_STOCK,
    OVERSTOCK,
    EXPIRING_SOON
}

enum class OrderStatus {
    DRAFT,
    PENDING,
    APPROVED,
    ORDERED,
    PARTIALLY_RECEIVED,
    RECEIVED,
    CANCELLED
}
