package com.mopanesystems.ziposmobile.data.repository

import com.mopanesystems.ziposmobile.data.database.dao.CustomerDao
import com.mopanesystems.ziposmobile.data.model.Customer
import com.mopanesystems.ziposmobile.data.model.CustomerTransaction
import com.mopanesystems.ziposmobile.data.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val customerDao: CustomerDao
) {
    fun getAllActiveCustomers(): Flow<List<Customer>> = customerDao.getAllActiveCustomers()
    
    fun getCustomersByStore(storeId: String): Flow<List<Customer>> = customerDao.getCustomersByStore(storeId)
    
    suspend fun getCustomerById(id: String): Customer? = customerDao.getCustomerById(id)
    
    suspend fun getCustomerByPhone(phone: String): Customer? = customerDao.getCustomerByPhone(phone)
    
    suspend fun getCustomerByEmail(email: String): Customer? = customerDao.getCustomerByEmail(email)
    
    fun searchCustomers(searchQuery: String): Flow<List<Customer>> = customerDao.searchCustomers("%$searchQuery%")
    
    fun getCustomersWithLoyaltyPoints(): Flow<List<Customer>> = customerDao.getCustomersWithLoyaltyPoints()
    
    fun getCustomersWithBalance(): Flow<List<Customer>> = customerDao.getCustomersWithBalance()
    
    fun getHighRiskCustomers(threshold: Int = 70): Flow<List<Customer>> = customerDao.getHighRiskCustomers(threshold)
    
    suspend fun insertCustomer(customer: Customer) = customerDao.insertCustomer(customer)
    
    suspend fun insertCustomers(customers: List<Customer>) = customerDao.insertCustomers(customers)
    
    suspend fun updateCustomer(customer: Customer) = customerDao.updateCustomer(customer)
    
    suspend fun deleteCustomer(customer: Customer) = customerDao.deleteCustomer(customer)
    
    suspend fun deactivateCustomer(customerId: String) = customerDao.deactivateCustomer(customerId)
    
    suspend fun updateLoyaltyPoints(customerId: String, points: Int) = customerDao.updateLoyaltyPoints(customerId, points)
    
    suspend fun updateBalance(customerId: String, balance: BigDecimal) = customerDao.updateBalance(customerId, balance)
    
    suspend fun updateTotalSpent(customerId: String, totalSpent: BigDecimal) = customerDao.updateTotalSpent(customerId, totalSpent)
    
    suspend fun updateRiskScore(customerId: String, riskScore: Int) = customerDao.updateRiskScore(customerId, riskScore)
    
    // Customer Transactions
    fun getCustomerTransactions(customerId: String): Flow<List<CustomerTransaction>> = customerDao.getCustomerTransactions(customerId)
    
    fun getCustomerTransactionsByType(customerId: String, type: TransactionType): Flow<List<CustomerTransaction>> = 
        customerDao.getCustomerTransactionsByType(customerId, type)
    
    suspend fun insertCustomerTransaction(transaction: CustomerTransaction) = customerDao.insertCustomerTransaction(transaction)
    
    suspend fun getActiveCustomerCount(): Int = customerDao.getActiveCustomerCount()
}
