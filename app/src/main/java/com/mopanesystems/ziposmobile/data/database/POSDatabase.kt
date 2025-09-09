package com.mopanesystems.ziposmobile.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.mopanesystems.ziposmobile.data.database.dao.*
import com.mopanesystems.ziposmobile.data.database.converters.Converters
import com.mopanesystems.ziposmobile.data.model.*

@Database(
    entities = [
        Product::class,
        Category::class,
        Subcategory::class,
        Customer::class,
        CustomerTransaction::class,
        Transaction::class,
        TransactionItem::class,
        Payment::class,
        Store::class,
        User::class,
        UserPermission::class,
        InventoryAdjustment::class,
        StockAlert::class,
        Supplier::class,
        PurchaseOrder::class,
        PurchaseOrderItem::class,
        DailySales::class,
        ProductPerformance::class,
        CustomerAnalytics::class,
        RefundAnalytics::class,
        SyncLog::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class POSDatabase : RoomDatabase() {
    
    abstract fun productDao(): ProductDao
    abstract fun customerDao(): CustomerDao
    abstract fun transactionDao(): TransactionDao
    abstract fun storeDao(): StoreDao
    abstract fun userDao(): UserDao
    abstract fun userPermissionDao(): UserPermissionDao
    abstract fun inventoryAdjustmentDao(): InventoryAdjustmentDao
    abstract fun stockAlertDao(): StockAlertDao
    abstract fun supplierDao(): SupplierDao
    abstract fun purchaseOrderDao(): PurchaseOrderDao
    abstract fun purchaseOrderItemDao(): PurchaseOrderItemDao
    abstract fun dailySalesDao(): DailySalesDao
    abstract fun productPerformanceDao(): ProductPerformanceDao
    abstract fun customerAnalyticsDao(): CustomerAnalyticsDao
    abstract fun refundAnalyticsDao(): RefundAnalyticsDao
    abstract fun syncLogDao(): SyncLogDao

    companion object {
        @Volatile
        private var INSTANCE: POSDatabase? = null

        fun getDatabase(context: Context): POSDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    POSDatabase::class.java,
                    "pos_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
