package com.mopanesystems.ziposmobile.di

import com.mopanesystems.ziposmobile.data.database.dao.*
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
    fun provideProductRepository(
        productDao: ProductDao
    ): ProductRepository = ProductRepository(productDao)

    @Provides
    @Singleton
    fun provideCustomerRepository(
        customerDao: CustomerDao
    ): CustomerRepository = CustomerRepository(customerDao)

    @Provides
    @Singleton
    fun provideTransactionRepository(
        transactionDao: TransactionDao
    ): TransactionRepository = TransactionRepository(transactionDao)

    @Provides
    @Singleton
    fun provideStoreRepository(
        storeDao: StoreDao,
        userDao: UserDao,
        userPermissionDao: UserPermissionDao
    ): StoreRepository = StoreRepository(storeDao, userDao, userPermissionDao)

    @Provides
    @Singleton
    fun provideInventoryRepository(
        inventoryAdjustmentDao: InventoryAdjustmentDao,
        stockAlertDao: StockAlertDao,
        supplierDao: SupplierDao,
        purchaseOrderDao: PurchaseOrderDao,
        purchaseOrderItemDao: PurchaseOrderItemDao,
        productDao: ProductDao
    ): InventoryRepository = InventoryRepository(
        inventoryAdjustmentDao,
        stockAlertDao,
        supplierDao,
        purchaseOrderDao,
        purchaseOrderItemDao,
        productDao
    )

    @Provides
    @Singleton
    fun provideAnalyticsRepository(
        dailySalesDao: DailySalesDao,
        productPerformanceDao: ProductPerformanceDao,
        customerAnalyticsDao: CustomerAnalyticsDao,
        refundAnalyticsDao: RefundAnalyticsDao,
        syncLogDao: SyncLogDao
    ): AnalyticsRepository = AnalyticsRepository(
        dailySalesDao,
        productPerformanceDao,
        customerAnalyticsDao,
        refundAnalyticsDao,
        syncLogDao
    )
}
