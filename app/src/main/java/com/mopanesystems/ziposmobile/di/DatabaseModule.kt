package com.mopanesystems.ziposmobile.di

import android.content.Context
import androidx.room.Room
import com.mopanesystems.ziposmobile.data.database.POSDatabase
import com.mopanesystems.ziposmobile.data.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePOSDatabase(@ApplicationContext context: Context): POSDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            POSDatabase::class.java,
            "pos_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideProductDao(database: POSDatabase): ProductDao = database.productDao()

    @Provides
    fun provideCustomerDao(database: POSDatabase): CustomerDao = database.customerDao()

    @Provides
    fun provideTransactionDao(database: POSDatabase): TransactionDao = database.transactionDao()

    @Provides
    fun provideStoreDao(database: POSDatabase): StoreDao = database.storeDao()

    @Provides
    fun provideUserDao(database: POSDatabase): UserDao = database.userDao()

    @Provides
    fun provideUserPermissionDao(database: POSDatabase): UserPermissionDao = database.userPermissionDao()

    @Provides
    fun provideInventoryAdjustmentDao(database: POSDatabase): InventoryAdjustmentDao = database.inventoryAdjustmentDao()

    @Provides
    fun provideStockAlertDao(database: POSDatabase): StockAlertDao = database.stockAlertDao()

    @Provides
    fun provideSupplierDao(database: POSDatabase): SupplierDao = database.supplierDao()

    @Provides
    fun providePurchaseOrderDao(database: POSDatabase): PurchaseOrderDao = database.purchaseOrderDao()

    @Provides
    fun providePurchaseOrderItemDao(database: POSDatabase): PurchaseOrderItemDao = database.purchaseOrderItemDao()

    @Provides
    fun provideDailySalesDao(database: POSDatabase): DailySalesDao = database.dailySalesDao()

    @Provides
    fun provideProductPerformanceDao(database: POSDatabase): ProductPerformanceDao = database.productPerformanceDao()

    @Provides
    fun provideCustomerAnalyticsDao(database: POSDatabase): CustomerAnalyticsDao = database.customerAnalyticsDao()

    @Provides
    fun provideRefundAnalyticsDao(database: POSDatabase): RefundAnalyticsDao = database.refundAnalyticsDao()

    @Provides
    fun provideSyncLogDao(database: POSDatabase): SyncLogDao = database.syncLogDao()
}
