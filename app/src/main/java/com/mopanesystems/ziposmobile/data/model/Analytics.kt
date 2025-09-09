package com.mopanesystems.ziposmobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "daily_sales")
data class DailySales(
    @PrimaryKey
    val id: String,
    val date: LocalDateTime,
    val storeId: String,
    val totalSales: BigDecimal,
    val totalTransactions: Int,
    val totalItems: Int,
    val averageTransactionValue: BigDecimal,
    val cashSales: BigDecimal,
    val cardSales: BigDecimal,
    val mobileSales: BigDecimal,
    val otherSales: BigDecimal,
    val refunds: BigDecimal,
    val discounts: BigDecimal,
    val taxes: BigDecimal,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "product_performance")
data class ProductPerformance(
    @PrimaryKey
    val id: String,
    val productId: String,
    val storeId: String,
    val period: String, // daily, weekly, monthly
    val periodStart: LocalDateTime,
    val periodEnd: LocalDateTime,
    val quantitySold: Int,
    val revenue: BigDecimal,
    val profit: BigDecimal,
    val averagePrice: BigDecimal,
    val rank: Int,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "customer_analytics")
data class CustomerAnalytics(
    @PrimaryKey
    val id: String,
    val customerId: String,
    val storeId: String,
    val period: String,
    val periodStart: LocalDateTime,
    val periodEnd: LocalDateTime,
    val totalSpent: BigDecimal,
    val totalTransactions: Int,
    val averageTransactionValue: BigDecimal,
    val lastPurchaseDate: LocalDateTime? = null,
    val riskScore: Int = 0,
    val loyaltyTier: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "refund_analytics")
data class RefundAnalytics(
    @PrimaryKey
    val id: String,
    val storeId: String,
    val period: String,
    val periodStart: LocalDateTime,
    val periodEnd: LocalDateTime,
    val totalRefunds: BigDecimal,
    val refundCount: Int,
    val averageRefundAmount: BigDecimal,
    val topRefundReasons: String, // JSON string of reasons
    val refundRate: Double, // percentage
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "sync_logs")
data class SyncLog(
    @PrimaryKey
    val id: String,
    val storeId: String,
    val syncType: SyncType,
    val status: SyncStatus,
    val recordsProcessed: Int,
    val recordsFailed: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null,
    val errorMessage: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class SyncType {
    PRODUCTS,
    CUSTOMERS,
    TRANSACTIONS,
    INVENTORY,
    ANALYTICS,
    FULL_SYNC
}

enum class SyncStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    CANCELLED
}
