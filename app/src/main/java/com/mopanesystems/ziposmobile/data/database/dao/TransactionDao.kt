package com.mopanesystems.ziposmobile.data.database.dao

import androidx.room.*
import com.mopanesystems.ziposmobile.data.model.Transaction
import com.mopanesystems.ziposmobile.data.model.TransactionStatus
import com.mopanesystems.ziposmobile.data.model.TransactionItem
import com.mopanesystems.ziposmobile.data.model.Payment
import com.mopanesystems.ziposmobile.data.model.PaymentMethod
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE storeId = :storeId ORDER BY createdAt DESC")
    fun getTransactionsByStore(storeId: String): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: String): Transaction?

    @Query("SELECT * FROM transactions WHERE transactionNumber = :transactionNumber")
    suspend fun getTransactionByNumber(transactionNumber: String): Transaction?

    @Query("SELECT * FROM transactions WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun getTransactionsByCustomer(customerId: String): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE cashierId = :cashierId ORDER BY createdAt DESC")
    fun getTransactionsByCashier(cashierId: String): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE status = :status ORDER BY createdAt DESC")
    fun getTransactionsByStatus(status: TransactionStatus): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE DATE(createdAt) = DATE(:date) ORDER BY createdAt DESC")
    fun getTransactionsByDate(date: LocalDateTime): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE createdAt BETWEEN :startDate AND :endDate ORDER BY createdAt DESC")
    fun getTransactionsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE isRefund = 1 ORDER BY createdAt DESC")
    fun getRefundTransactions(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("UPDATE transactions SET status = :status WHERE id = :transactionId")
    suspend fun updateTransactionStatus(transactionId: String, status: TransactionStatus)

    @Query("UPDATE transactions SET completedAt = :completedAt WHERE id = :transactionId")
    suspend fun markTransactionCompleted(transactionId: String, completedAt: LocalDateTime)

    // Transaction Items
    @Query("SELECT * FROM transaction_items WHERE transactionId = :transactionId")
    fun getTransactionItems(transactionId: String): Flow<List<TransactionItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionItem(item: TransactionItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionItems(items: List<TransactionItem>)

    @Update
    suspend fun updateTransactionItem(item: TransactionItem)

    @Delete
    suspend fun deleteTransactionItem(item: TransactionItem)

    @Query("DELETE FROM transaction_items WHERE transactionId = :transactionId")
    suspend fun deleteTransactionItems(transactionId: String)

    // Payments
    @Query("SELECT * FROM payments WHERE transactionId = :transactionId")
    fun getPayments(transactionId: String): Flow<List<Payment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: Payment)

    @Update
    suspend fun updatePayment(payment: Payment)

    @Delete
    suspend fun deletePayment(payment: Payment)

    // Analytics Queries
    @Query("SELECT SUM(totalAmount) FROM transactions WHERE status = 'COMPLETED' AND DATE(createdAt) = DATE(:date)")
    suspend fun getDailySalesTotal(date: LocalDateTime): BigDecimal?

    @Query("SELECT COUNT(*) FROM transactions WHERE status = 'COMPLETED' AND DATE(createdAt) = DATE(:date)")
    suspend fun getDailyTransactionCount(date: LocalDateTime): Int

    @Query("SELECT SUM(totalAmount) FROM transactions WHERE status = 'COMPLETED' AND createdAt BETWEEN :startDate AND :endDate")
    suspend fun getSalesTotalByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal?

    @Query("SELECT COUNT(*) FROM transactions WHERE status = 'COMPLETED' AND createdAt BETWEEN :startDate AND :endDate")
    suspend fun getTransactionCountByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Int

    @Query("SELECT AVG(totalAmount) FROM transactions WHERE status = 'COMPLETED' AND createdAt BETWEEN :startDate AND :endDate")
    suspend fun getAverageTransactionValue(startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal?

    @Query("SELECT SUM(totalAmount) FROM transactions WHERE status = 'COMPLETED' AND paymentMethod = :paymentMethod AND createdAt BETWEEN :startDate AND :endDate")
    suspend fun getSalesByPaymentMethod(paymentMethod: PaymentMethod, startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal?

    // Offline Support
    @Query("SELECT * FROM transactions WHERE status = 'PENDING' ORDER BY createdAt ASC")
    fun getQueuedTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE isSynced = 0 ORDER BY createdAt ASC")
    fun getUnsyncedTransactions(): Flow<List<Transaction>>

    @Query("UPDATE transactions SET isSynced = 1 WHERE id = :transactionId")
    suspend fun markTransactionAsSynced(transactionId: String)
}
