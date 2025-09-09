package com.mopanesystems.ziposmobile.data.database.dao

import androidx.room.*
import com.mopanesystems.ziposmobile.data.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface DailySalesDao {
    @Query("SELECT * FROM daily_sales ORDER BY date DESC")
    fun getAllDailySales(): Flow<List<DailySales>>

    @Query("SELECT * FROM daily_sales WHERE storeId = :storeId ORDER BY date DESC")
    fun getDailySalesByStore(storeId: String): Flow<List<DailySales>>

    @Query("SELECT * FROM daily_sales WHERE date = :date")
    suspend fun getDailySalesByDate(date: LocalDateTime): DailySales?

    @Query("SELECT * FROM daily_sales WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getDailySalesByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<DailySales>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailySales(dailySales: DailySales)

    @Update
    suspend fun updateDailySales(dailySales: DailySales)

    @Delete
    suspend fun deleteDailySales(dailySales: DailySales)
}

@Dao
interface ProductPerformanceDao {
    @Query("SELECT * FROM product_performance ORDER BY revenue DESC")
    fun getAllProductPerformance(): Flow<List<ProductPerformance>>

    @Query("SELECT * FROM product_performance WHERE storeId = :storeId ORDER BY revenue DESC")
    fun getProductPerformanceByStore(storeId: String): Flow<List<ProductPerformance>>

    @Query("SELECT * FROM product_performance WHERE productId = :productId ORDER BY periodStart DESC")
    fun getProductPerformanceByProduct(productId: String): Flow<List<ProductPerformance>>

    @Query("SELECT * FROM product_performance WHERE period = :period AND periodStart BETWEEN :startDate AND :endDate ORDER BY revenue DESC")
    fun getProductPerformanceByPeriod(period: String, startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<ProductPerformance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductPerformance(performance: ProductPerformance)

    @Update
    suspend fun updateProductPerformance(performance: ProductPerformance)

    @Delete
    suspend fun deleteProductPerformance(performance: ProductPerformance)
}

@Dao
interface CustomerAnalyticsDao {
    @Query("SELECT * FROM customer_analytics ORDER BY totalSpent DESC")
    fun getAllCustomerAnalytics(): Flow<List<CustomerAnalytics>>

    @Query("SELECT * FROM customer_analytics WHERE storeId = :storeId ORDER BY totalSpent DESC")
    fun getCustomerAnalyticsByStore(storeId: String): Flow<List<CustomerAnalytics>>

    @Query("SELECT * FROM customer_analytics WHERE customerId = :customerId ORDER BY periodStart DESC")
    fun getCustomerAnalyticsByCustomer(customerId: String): Flow<List<CustomerAnalytics>>

    @Query("SELECT * FROM customer_analytics WHERE period = :period AND periodStart BETWEEN :startDate AND :endDate ORDER BY totalSpent DESC")
    fun getCustomerAnalyticsByPeriod(period: String, startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<CustomerAnalytics>>

    @Query("SELECT * FROM customer_analytics WHERE loyaltyTier = :tier ORDER BY totalSpent DESC")
    fun getCustomerAnalyticsByTier(tier: String): Flow<List<CustomerAnalytics>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerAnalytics(analytics: CustomerAnalytics)

    @Update
    suspend fun updateCustomerAnalytics(analytics: CustomerAnalytics)

    @Delete
    suspend fun deleteCustomerAnalytics(analytics: CustomerAnalytics)
}

@Dao
interface RefundAnalyticsDao {
    @Query("SELECT * FROM refund_analytics ORDER BY periodStart DESC")
    fun getAllRefundAnalytics(): Flow<List<RefundAnalytics>>

    @Query("SELECT * FROM refund_analytics WHERE storeId = :storeId ORDER BY periodStart DESC")
    fun getRefundAnalyticsByStore(storeId: String): Flow<List<RefundAnalytics>>

    @Query("SELECT * FROM refund_analytics WHERE period = :period AND periodStart BETWEEN :startDate AND :endDate ORDER BY periodStart DESC")
    fun getRefundAnalyticsByPeriod(period: String, startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<RefundAnalytics>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRefundAnalytics(analytics: RefundAnalytics)

    @Update
    suspend fun updateRefundAnalytics(analytics: RefundAnalytics)

    @Delete
    suspend fun deleteRefundAnalytics(analytics: RefundAnalytics)
}

@Dao
interface SyncLogDao {
    @Query("SELECT * FROM sync_logs ORDER BY createdAt DESC")
    fun getAllSyncLogs(): Flow<List<SyncLog>>

    @Query("SELECT * FROM sync_logs WHERE storeId = :storeId ORDER BY createdAt DESC")
    fun getSyncLogsByStore(storeId: String): Flow<List<SyncLog>>

    @Query("SELECT * FROM sync_logs WHERE syncType = :syncType ORDER BY createdAt DESC")
    fun getSyncLogsByType(syncType: SyncType): Flow<List<SyncLog>>

    @Query("SELECT * FROM sync_logs WHERE status = :status ORDER BY createdAt DESC")
    fun getSyncLogsByStatus(status: SyncStatus): Flow<List<SyncLog>>

    @Query("SELECT * FROM sync_logs WHERE id = :id")
    suspend fun getSyncLogById(id: String): SyncLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncLog(log: SyncLog)

    @Update
    suspend fun updateSyncLog(log: SyncLog)

    @Delete
    suspend fun deleteSyncLog(log: SyncLog)

    @Query("SELECT * FROM sync_logs WHERE status = 'IN_PROGRESS'")
    suspend fun getActiveSyncLogs(): List<SyncLog>

    @Query("SELECT * FROM sync_logs ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastSyncLog(): SyncLog?
}
