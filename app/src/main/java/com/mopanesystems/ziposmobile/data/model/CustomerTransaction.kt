package com.mopanesystems.ziposmobile.data.model

import androidx.room.ColumnInfo
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
    @ColumnInfo(name = "type")
    val type: TransactionType,
    val amount: BigDecimal,
    val balance: BigDecimal,
    val description: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
