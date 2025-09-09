package com.mopanesystems.ziposmobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "customer_transactions")
data class CustomerTransaction(
    @PrimaryKey
    val id: String,
    val customerId: String,
    val transactionId: String,
    val pointsEarned: Int = 0,
    val pointsRedeemed: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "inventory_adjustments")
data class InventoryAdjustment(
    @PrimaryKey
    val id: String,
    val productId: String,
    val quantity: Int,
    val reason: AdjustmentReason,
    val notes: String? = null,
    val adjustedBy: String,
    val storeId: String,
    val isSynced: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "stock_alerts")
data class StockAlert(
    @PrimaryKey
    val id: String,
    val productId: String,
    val currentStock: Int,
    val minThreshold: Int,
    val alertType: StockAlertType,
    val isActive: Boolean = true,
    val resolvedBy: String? = null,
    val resolvedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
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
    val status: PurchaseOrderStatus,
    val totalAmount: BigDecimal,
    val notes: String? = null,
    val orderedBy: String,
    val receivedBy: String? = null,
    val orderedAt: LocalDateTime = LocalDateTime.now(),
    val expectedDeliveryAt: LocalDateTime? = null,
    val receivedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val storeId: String
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
    val receivedQuantity: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "daily_sales")
data class DailySales(
    @PrimaryKey
    val id: String,
    val date: LocalDateTime,
    val totalSales: BigDecimal,
    val totalTransactions: Int,
    val averageTransactionValue: BigDecimal,
    val totalTax: BigDecimal,
    val totalDiscount: BigDecimal,
    val storeId: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "product_performance")
data class ProductPerformance(
    @PrimaryKey
    val id: String,
    val productId: String,
    val period: String, // "daily", "weekly", "monthly"
    val periodDate: LocalDateTime,
    val quantitySold: Int,
    val revenue: BigDecimal,
    val profit: BigDecimal,
    val storeId: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "customer_analytics")
data class CustomerAnalytics(
    @PrimaryKey
    val id: String,
    val customerId: String,
    val period: String, // "daily", "weekly", "monthly"
    val periodDate: LocalDateTime,
    val totalSpent: BigDecimal,
    val transactionCount: Int,
    val averageTransactionValue: BigDecimal,
    val lastPurchaseDate: LocalDateTime? = null,
    val storeId: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "refund_analytics")
data class RefundAnalytics(
    @PrimaryKey
    val id: String,
    val period: String, // "daily", "weekly", "monthly"
    val periodDate: LocalDateTime,
    val totalRefunds: BigDecimal,
    val refundCount: Int,
    val averageRefundValue: BigDecimal,
    val refundReasons: String, // JSON string of reasons and counts
    val storeId: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "sync_logs")
data class SyncLog(
    @PrimaryKey
    val id: String,
    val syncType: String, // "products", "customers", "transactions", etc.
    val status: SyncStatus,
    val recordsProcessed: Int,
    val recordsFailed: Int,
    val errorMessage: String? = null,
    val startedAt: LocalDateTime,
    val completedAt: LocalDateTime? = null,
    val storeId: String
)

enum class StockAlertType {
    LOW_STOCK,
    OUT_OF_STOCK,
    OVERSTOCK,
    EXPIRING_SOON
}

enum class PurchaseOrderStatus {
    DRAFT,
    PENDING,
    ORDERED,
    PARTIALLY_RECEIVED,
    RECEIVED,
    CANCELLED
}

enum class SyncStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    CANCELLED
}

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

enum class OrderStatus {
    DRAFT,
    PENDING,
    APPROVED,
    ORDERED,
    PARTIALLY_RECEIVED,
    RECEIVED,
    CANCELLED
}
