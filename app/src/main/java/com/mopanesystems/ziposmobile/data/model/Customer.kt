package com.mopanesystems.ziposmobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zipCode: String? = null,
    val country: String? = null,
    val dateOfBirth: LocalDateTime? = null,
    val loyaltyPoints: Int = 0,
    val totalSpent: BigDecimal = BigDecimal.ZERO,
    val balance: BigDecimal = BigDecimal.ZERO,
    val creditLimit: BigDecimal? = null,
    val isActive: Boolean = true,
    val riskScore: Int = 0,
    val notes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val storeId: String? = null
) {
    val fullName: String
        get() = "$firstName $lastName"
}

// CustomerTransaction entity is defined in CustomerTransaction.kt to avoid duplication

// TransactionType enum is defined in Transaction.kt to avoid duplication
