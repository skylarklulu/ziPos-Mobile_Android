package com.mopanesystems.ziposmobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "stores")
data class Store(
    @PrimaryKey
    val id: String,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val phone: String? = null,
    val email: String? = null,
    val website: String? = null,
    val taxId: String? = null,
    val currency: String = "USD",
    val timezone: String = "UTC",
    val isActive: Boolean = true,
    val settings: StoreSettings? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val storeId: String? = null,
    val isActive: Boolean = true,
    val lastLoginAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    val fullName: String
        get() = "$firstName $lastName"
}

@Entity(tableName = "user_permissions")
data class UserPermission(
    @PrimaryKey
    val id: String,
    val userId: String,
    val permission: String,
    val isGranted: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class StoreSettings(
    val receiptHeader: String? = null,
    val receiptFooter: String? = null,
    val taxRate: Double = 0.0,
    val allowNegativeStock: Boolean = false,
    val requireManagerApproval: Boolean = false,
    val autoPrintReceipts: Boolean = true,
    val enableLoyaltyProgram: Boolean = false,
    val loyaltyPointsPerDollar: Double = 1.0,
    val lowStockThreshold: Int = 10,
    val enableOfflineMode: Boolean = true,
    val syncInterval: Int = 300 // seconds
)

enum class UserRole {
    ADMIN,
    MANAGER,
    CASHIER,
    VIEWER
}
