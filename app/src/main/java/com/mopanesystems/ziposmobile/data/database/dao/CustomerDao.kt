package com.mopanesystems.ziposmobile.data.database.dao

import androidx.room.*
import com.mopanesystems.ziposmobile.data.model.Customer
import com.mopanesystems.ziposmobile.data.model.CustomerTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers WHERE isActive = 1 ORDER BY lastName, firstName ASC")
    fun getAllActiveCustomers(): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE storeId = :storeId AND isActive = 1 ORDER BY lastName, firstName ASC")
    fun getCustomersByStore(storeId: String): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: String): Customer?

    @Query("SELECT * FROM customers WHERE phone = :phone AND isActive = 1")
    suspend fun getCustomerByPhone(phone: String): Customer?

    @Query("SELECT * FROM customers WHERE email = :email AND isActive = 1")
    suspend fun getCustomerByEmail(email: String): Customer?

    @Query("SELECT * FROM customers WHERE (firstName LIKE :searchQuery OR lastName LIKE :searchQuery OR phone LIKE :searchQuery OR email LIKE :searchQuery) AND isActive = 1 ORDER BY lastName, firstName ASC")
    fun searchCustomers(searchQuery: String): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE loyaltyPoints > 0 AND isActive = 1 ORDER BY loyaltyPoints DESC")
    fun getCustomersWithLoyaltyPoints(): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE balance != 0 AND isActive = 1 ORDER BY balance DESC")
    fun getCustomersWithBalance(): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE riskScore > :threshold AND isActive = 1 ORDER BY riskScore DESC")
    fun getHighRiskCustomers(threshold: Int = 70): Flow<List<Customer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<Customer>)

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("UPDATE customers SET isActive = 0 WHERE id = :customerId")
    suspend fun deactivateCustomer(customerId: String)

    @Query("UPDATE customers SET loyaltyPoints = :points WHERE id = :customerId")
    suspend fun updateLoyaltyPoints(customerId: String, points: Int)

    @Query("UPDATE customers SET balance = :balance WHERE id = :customerId")
    suspend fun updateBalance(customerId: String, balance: java.math.BigDecimal)

    @Query("UPDATE customers SET totalSpent = :totalSpent WHERE id = :customerId")
    suspend fun updateTotalSpent(customerId: String, totalSpent: java.math.BigDecimal)

    @Query("UPDATE customers SET riskScore = :riskScore WHERE id = :customerId")
    suspend fun updateRiskScore(customerId: String, riskScore: Int)

    // Customer Transactions
    @Query("SELECT * FROM customer_transactions WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun getCustomerTransactions(customerId: String): Flow<List<CustomerTransaction>>

    @Query("SELECT * FROM customer_transactions WHERE customerId = :customerId AND type = :type ORDER BY createdAt DESC")
    fun getCustomerTransactionsByType(customerId: String, type: com.mopanesystems.ziposmobile.data.model.TransactionType): Flow<List<CustomerTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerTransaction(transaction: CustomerTransaction)

    @Query("SELECT COUNT(*) FROM customers WHERE isActive = 1")
    suspend fun getActiveCustomerCount(): Int
}
