package com.mopanesystems.ziposmobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String? = null,
    val sku: String,
    val barcode: String? = null,
    val price: BigDecimal,
    val cost: BigDecimal? = null,
    val categoryId: String? = null,
    val subcategoryId: String? = null,
    val stockQuantity: Int = 0,
    val minStockLevel: Int = 0,
    val maxStockLevel: Int? = null,
    val isActive: Boolean = true,
    val isTaxable: Boolean = true,
    val taxRate: BigDecimal = BigDecimal.ZERO,
    val imageUrl: String? = null,
    val weight: BigDecimal? = null,
    val dimensions: String? = null,
    val supplierId: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val storeId: String? = null
)

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String? = null,
    val parentId: String? = null,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val storeId: String? = null
)

@Entity(tableName = "subcategories")
data class Subcategory(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String? = null,
    val categoryId: String,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val storeId: String? = null
)
