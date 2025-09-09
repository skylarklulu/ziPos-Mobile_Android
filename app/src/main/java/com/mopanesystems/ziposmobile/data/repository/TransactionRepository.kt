package com.mopanesystems.ziposmobile.data.repository

import com.mopanesystems.ziposmobile.data.database.dao.TransactionDao
import com.mopanesystems.ziposmobile.data.model.*
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()
    
    fun getTransactionsByStore(storeId: String): Flow<List<Transaction>> = transactionDao.getTransactionsByStore(storeId)
    
    suspend fun getTransactionById(id: String): Transaction? = transactionDao.getTransactionById(id)
    
    suspend fun getTransactionByNumber(transactionNumber: String): Transaction? = transactionDao.getTransactionByNumber(transactionNumber)
    
    fun getTransactionsByCustomer(customerId: String): Flow<List<Transaction>> = transactionDao.getTransactionsByCustomer(customerId)
    
    fun getTransactionsByCashier(cashierId: String): Flow<List<Transaction>> = transactionDao.getTransactionsByCashier(cashierId)
    
    fun getTransactionsByStatus(status: TransactionStatus): Flow<List<Transaction>> = transactionDao.getTransactionsByStatus(status)
    
    fun getTransactionsByDate(date: LocalDateTime): Flow<List<Transaction>> = transactionDao.getTransactionsByDate(date)
    
    fun getTransactionsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByDateRange(startDate, endDate)
    
    fun getRefundTransactions(): Flow<List<Transaction>> = transactionDao.getRefundTransactions()
    
    suspend fun insertTransaction(transaction: Transaction) = transactionDao.insertTransaction(transaction)
    
    suspend fun updateTransaction(transaction: Transaction) = transactionDao.updateTransaction(transaction)
    
    suspend fun deleteTransaction(transaction: Transaction) = transactionDao.deleteTransaction(transaction)
    
    suspend fun updateTransactionStatus(transactionId: String, status: TransactionStatus) = 
        transactionDao.updateTransactionStatus(transactionId, status)
    
    suspend fun markTransactionCompleted(transactionId: String, completedAt: LocalDateTime) = 
        transactionDao.markTransactionCompleted(transactionId, completedAt)
    
    // Transaction Items
    fun getTransactionItems(transactionId: String): Flow<List<TransactionItem>> = transactionDao.getTransactionItems(transactionId)
    
    suspend fun insertTransactionItem(item: TransactionItem) = transactionDao.insertTransactionItem(item)
    
    suspend fun insertTransactionItems(items: List<TransactionItem>) = transactionDao.insertTransactionItems(items)
    
    suspend fun updateTransactionItem(item: TransactionItem) = transactionDao.updateTransactionItem(item)
    
    suspend fun deleteTransactionItem(item: TransactionItem) = transactionDao.deleteTransactionItem(item)
    
    suspend fun deleteTransactionItems(transactionId: String) = transactionDao.deleteTransactionItems(transactionId)
    
    // Payments
    fun getPayments(transactionId: String): Flow<List<Payment>> = transactionDao.getPayments(transactionId)
    
    suspend fun insertPayment(payment: Payment) = transactionDao.insertPayment(payment)
    
    suspend fun updatePayment(payment: Payment) = transactionDao.updatePayment(payment)
    
    suspend fun deletePayment(payment: Payment) = transactionDao.deletePayment(payment)
    
    // Analytics
    suspend fun getDailySalesTotal(date: LocalDateTime): BigDecimal? = transactionDao.getDailySalesTotal(date)
    
    suspend fun getDailyTransactionCount(date: LocalDateTime): Int = transactionDao.getDailyTransactionCount(date)
    
    suspend fun getSalesTotalByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal? = 
        transactionDao.getSalesTotalByDateRange(startDate, endDate)
    
    suspend fun getTransactionCountByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Int = 
        transactionDao.getTransactionCountByDateRange(startDate, endDate)
    
    suspend fun getAverageTransactionValue(startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal? = 
        transactionDao.getAverageTransactionValue(startDate, endDate)
    
    suspend fun getSalesByPaymentMethod(paymentMethod: PaymentMethod, startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal? = 
        transactionDao.getSalesByPaymentMethod(paymentMethod, startDate, endDate)
}
