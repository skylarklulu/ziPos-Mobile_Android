package com.mopanesystems.ziposmobile.data.repository

import com.mopanesystems.ziposmobile.data.database.dao.*
import com.mopanesystems.ziposmobile.data.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepository @Inject constructor(
    private val dailySalesDao: DailySalesDao,
    private val productPerformanceDao: ProductPerformanceDao,
    private val customerAnalyticsDao: CustomerAnalyticsDao,
    private val refundAnalyticsDao: RefundAnalyticsDao,
    private val syncLogDao: SyncLogDao
) {
    // Daily Sales
    fun getAllDailySales(): Flow<List<DailySales>> = dailySalesDao.getAllDailySales()
    
    fun getDailySalesByStore(storeId: String): Flow<List<DailySales>> = dailySalesDao.getDailySalesByStore(storeId)
    
    suspend fun getDailySalesByDate(date: LocalDateTime): DailySales? = dailySalesDao.getDailySalesByDate(date)
    
    fun getDailySalesByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<DailySales>> = 
        dailySalesDao.getDailySalesByDateRange(startDate, endDate)
    
    suspend fun insertDailySales(dailySales: DailySales) = dailySalesDao.insertDailySales(dailySales)
    
    suspend fun updateDailySales(dailySales: DailySales) = dailySalesDao.updateDailySales(dailySales)
    
    suspend fun deleteDailySales(dailySales: DailySales) = dailySalesDao.deleteDailySales(dailySales)
    
    // Product Performance
    fun getAllProductPerformance(): Flow<List<ProductPerformance>> = productPerformanceDao.getAllProductPerformance()
    
    fun getProductPerformanceByStore(storeId: String): Flow<List<ProductPerformance>> = productPerformanceDao.getProductPerformanceByStore(storeId)
    
    fun getProductPerformanceByProduct(productId: String): Flow<List<ProductPerformance>> = productPerformanceDao.getProductPerformanceByProduct(productId)
    
    fun getProductPerformanceByPeriod(period: String, startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<ProductPerformance>> = 
        productPerformanceDao.getProductPerformanceByPeriod(period, startDate, endDate)
    
    suspend fun insertProductPerformance(performance: ProductPerformance) = productPerformanceDao.insertProductPerformance(performance)
    
    suspend fun updateProductPerformance(performance: ProductPerformance) = productPerformanceDao.updateProductPerformance(performance)
    
    suspend fun deleteProductPerformance(performance: ProductPerformance) = productPerformanceDao.deleteProductPerformance(performance)
    
    // Customer Analytics
    fun getAllCustomerAnalytics(): Flow<List<CustomerAnalytics>> = customerAnalyticsDao.getAllCustomerAnalytics()
    
    fun getCustomerAnalyticsByStore(storeId: String): Flow<List<CustomerAnalytics>> = customerAnalyticsDao.getCustomerAnalyticsByStore(storeId)
    
    fun getCustomerAnalyticsByCustomer(customerId: String): Flow<List<CustomerAnalytics>> = customerAnalyticsDao.getCustomerAnalyticsByCustomer(customerId)
    
    fun getCustomerAnalyticsByPeriod(period: String, startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<CustomerAnalytics>> = 
        customerAnalyticsDao.getCustomerAnalyticsByPeriod(period, startDate, endDate)
    
    fun getCustomerAnalyticsByTier(tier: String): Flow<List<CustomerAnalytics>> = customerAnalyticsDao.getCustomerAnalyticsByTier(tier)
    
    suspend fun insertCustomerAnalytics(analytics: CustomerAnalytics) = customerAnalyticsDao.insertCustomerAnalytics(analytics)
    
    suspend fun updateCustomerAnalytics(analytics: CustomerAnalytics) = customerAnalyticsDao.updateCustomerAnalytics(analytics)
    
    suspend fun deleteCustomerAnalytics(analytics: CustomerAnalytics) = customerAnalyticsDao.deleteCustomerAnalytics(analytics)
    
    // Refund Analytics
    fun getAllRefundAnalytics(): Flow<List<RefundAnalytics>> = refundAnalyticsDao.getAllRefundAnalytics()
    
    fun getRefundAnalyticsByStore(storeId: String): Flow<List<RefundAnalytics>> = refundAnalyticsDao.getRefundAnalyticsByStore(storeId)
    
    fun getRefundAnalyticsByPeriod(period: String, startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<RefundAnalytics>> = 
        refundAnalyticsDao.getRefundAnalyticsByPeriod(period, startDate, endDate)
    
    suspend fun insertRefundAnalytics(analytics: RefundAnalytics) = refundAnalyticsDao.insertRefundAnalytics(analytics)
    
    suspend fun updateRefundAnalytics(analytics: RefundAnalytics) = refundAnalyticsDao.updateRefundAnalytics(analytics)
    
    suspend fun deleteRefundAnalytics(analytics: RefundAnalytics) = refundAnalyticsDao.deleteRefundAnalytics(analytics)
    
    // Sync Logs
    fun getAllSyncLogs(): Flow<List<SyncLog>> = syncLogDao.getAllSyncLogs()
    
    fun getSyncLogsByStore(storeId: String): Flow<List<SyncLog>> = syncLogDao.getSyncLogsByStore(storeId)
    
    fun getSyncLogsByType(syncType: SyncType): Flow<List<SyncLog>> = syncLogDao.getSyncLogsByType(syncType)
    
    fun getSyncLogsByStatus(status: SyncStatus): Flow<List<SyncLog>> = syncLogDao.getSyncLogsByStatus(status)
    
    suspend fun getSyncLogById(id: String): SyncLog? = syncLogDao.getSyncLogById(id)
    
    suspend fun insertSyncLog(log: SyncLog) = syncLogDao.insertSyncLog(log)
    
    suspend fun updateSyncLog(log: SyncLog) = syncLogDao.updateSyncLog(log)
    
    suspend fun deleteSyncLog(log: SyncLog) = syncLogDao.deleteSyncLog(log)
    
    suspend fun getActiveSyncLogs(): List<SyncLog> = syncLogDao.getActiveSyncLogs()
}
