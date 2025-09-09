package com.mopanesystems.ziposmobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey
    val id: String,
    val transactionNumber: String,
    val customerId: String? = null,
    val cashierId: String,
    val storeId: String,
    val type: TransactionType,
    val status: TransactionStatus,
    val subtotal: BigDecimal,
    val taxAmount: BigDecimal,
    val discountAmount: BigDecimal = BigDecimal.ZERO,
    val totalAmount: BigDecimal,
    val paidAmount: BigDecimal = BigDecimal.ZERO,
    val changeAmount: BigDecimal = BigDecimal.ZERO,
    val paymentMethod: PaymentMethod? = null,
    val notes: String? = null,
    val isRefund: Boolean = false,
    val originalTransactionId: String? = null,
    val refundAmount: BigDecimal? = null,
    val refundReason: String? = null,
    val managerApprovalId: String? = null,
    val isSynced: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val completedAt: LocalDateTime? = null
)

@Entity(tableName = "transaction_items")
data class TransactionItem(
    @PrimaryKey
    val id: String,
    val transactionId: String,
    val productId: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal,
    val discountAmount: BigDecimal = BigDecimal.ZERO,
    val taxAmount: BigDecimal = BigDecimal.ZERO,
    val notes: String? = null
)

@Entity(tableName = "payments")
data class Payment(
    @PrimaryKey
    val id: String,
    val transactionId: String,
    val amount: BigDecimal,
    val method: PaymentMethod,
    val reference: String? = null,
    val status: PaymentStatus,
    val processedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class TransactionType {
    SALE,
    REFUND,
    RETURN,
    EXCHANGE,
    VOID,
    PURCHASE,
    DEPOSIT,
    WITHDRAWAL,
    PAYMENT,
    ADJUSTMENT
}

enum class TransactionStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    REFUNDED,
    PARTIALLY_REFUNDED
}

enum class PaymentMethod {
    CASH,
    CARD,
    MOBILE_PAYMENT,
    STORE_CREDIT,
    CHECK,
    BANK_TRANSFER,
    OTHER
}

enum class PaymentStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED,
    REFUNDED
}

