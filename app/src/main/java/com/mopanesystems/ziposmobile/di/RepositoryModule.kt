package com.mopanesystems.ziposmobile.di

import com.mopanesystems.ziposmobile.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(productDao: com.mopanesystems.ziposmobile.data.database.dao.ProductDao): ProductRepository {
        return ProductRepository(productDao)
    }

    @Provides
    @Singleton
    fun provideCustomerRepository(customerDao: com.mopanesystems.ziposmobile.data.database.dao.CustomerDao): CustomerRepository {
        return CustomerRepository(customerDao)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(transactionDao: com.mopanesystems.ziposmobile.data.database.dao.TransactionDao): TransactionRepository {
        return TransactionRepository(transactionDao)
    }

    @Provides
    @Singleton
    fun provideStoreRepository(
        storeDao: com.mopanesystems.ziposmobile.data.database.dao.StoreDao,
        userDao: com.mopanesystems.ziposmobile.data.database.dao.UserDao,
        userPermissionDao: com.mopanesystems.ziposmobile.data.database.dao.UserPermissionDao
    ): StoreRepository {
        return StoreRepository(storeDao, userDao, userPermissionDao)
    }

    @Provides
    @Singleton
    fun provideInventoryRepository(
        inventoryAdjustmentDao: com.mopanesystems.ziposmobile.data.database.dao.InventoryAdjustmentDao,
        stockAlertDao: com.mopanesystems.ziposmobile.data.database.dao.StockAlertDao,
        supplierDao: com.mopanesystems.ziposmobile.data.database.dao.SupplierDao,
        purchaseOrderDao: com.mopanesystems.ziposmobile.data.database.dao.PurchaseOrderDao,
        purchaseOrderItemDao: com.mopanesystems.ziposmobile.data.database.dao.PurchaseOrderItemDao
    ): InventoryRepository {
        return InventoryRepository(inventoryAdjustmentDao, stockAlertDao, supplierDao, purchaseOrderDao, purchaseOrderItemDao)
    }

    @Provides
    @Singleton
    fun provideAnalyticsRepository(
        dailySalesDao: com.mopanesystems.ziposmobile.data.database.dao.DailySalesDao,
        productPerformanceDao: com.mopanesystems.ziposmobile.data.database.dao.ProductPerformanceDao,
        customerAnalyticsDao: com.mopanesystems.ziposmobile.data.database.dao.CustomerAnalyticsDao,
        refundAnalyticsDao: com.mopanesystems.ziposmobile.data.database.dao.RefundAnalyticsDao,
        syncLogDao: com.mopanesystems.ziposmobile.data.database.dao.SyncLogDao
    ): AnalyticsRepository {
        return AnalyticsRepository(dailySalesDao, productPerformanceDao, customerAnalyticsDao, refundAnalyticsDao, syncLogDao)
    }

}
