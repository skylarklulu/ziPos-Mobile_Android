package com.mopanesystems.ziposmobile.ui.customers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mopanesystems.ziposmobile.data.model.Customer
import com.mopanesystems.ziposmobile.data.model.CustomerTransaction
import com.mopanesystems.ziposmobile.data.model.TransactionType
import com.mopanesystems.ziposmobile.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _customers = MutableStateFlow<List<Customer>>(emptyList())
    val customers: StateFlow<List<Customer>> = _customers.asStateFlow()

    private val _filteredCustomers = MutableStateFlow<List<Customer>>(emptyList())
    val filteredCustomers: StateFlow<List<Customer>> = _filteredCustomers.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCustomer = MutableStateFlow<Customer?>(null)
    val selectedCustomer: StateFlow<Customer?> = _selectedCustomer.asStateFlow()

    private val _customerTransactions = MutableStateFlow<List<CustomerTransaction>>(emptyList())
    val customerTransactions: StateFlow<List<CustomerTransaction>> = _customerTransactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadCustomers()
    }

    fun loadCustomers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                customerRepository.getAllActiveCustomers().collect { customerList ->
                    _customers.value = customerList
                    applyFilters()
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchCustomers(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun clearFilters() {
        _searchQuery.value = ""
        applyFilters()
    }

    private fun applyFilters() {
        val query = _searchQuery.value.lowercase()

        val filtered = _customers.value.filter { customer ->
            query.isEmpty() || 
                customer.firstName.lowercase().contains(query) ||
                customer.lastName.lowercase().contains(query) ||
                customer.phone?.lowercase()?.contains(query) == true ||
                customer.email?.lowercase()?.contains(query) == true
        }

        _filteredCustomers.value = filtered
    }

    fun selectCustomer(customer: Customer?) {
        _selectedCustomer.value = customer
        if (customer != null) {
            loadCustomerTransactions(customer.id)
        }
    }

    fun loadCustomerTransactions(customerId: String) {
        viewModelScope.launch {
            try {
                customerRepository.getCustomerTransactions(customerId).collect { transactions ->
                    _customerTransactions.value = transactions
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun addCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                customerRepository.insertCustomer(customer)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                val updatedCustomer = customer.copy(updatedAt = LocalDateTime.now())
                customerRepository.updateCustomer(updatedCustomer)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                customerRepository.deactivateCustomer(customer.id)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun updateCustomerBalance(customerId: String, amount: BigDecimal) {
        viewModelScope.launch {
            try {
                val customer = _customers.value.find { it.id == customerId }
                if (customer != null) {
                    val newBalance = customer.balance + amount
                    customerRepository.updateBalance(customerId, newBalance)
                    
                    // Add transaction record
                    val transaction = CustomerTransaction(
                        id = UUID.randomUUID().toString(),
                        customerId = customerId,
                        transactionId = "BALANCE_ADJUSTMENT_${System.currentTimeMillis()}",
                        type = if (amount > BigDecimal.ZERO) TransactionType.DEPOSIT else TransactionType.WITHDRAWAL,
                        amount = amount.abs(),
                        balance = newBalance,
                        description = "Balance adjustment"
                    )
                    customerRepository.insertCustomerTransaction(transaction)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun updateLoyaltyPoints(customerId: String, points: Int) {
        viewModelScope.launch {
            try {
                customerRepository.updateLoyaltyPoints(customerId, points)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun updateRiskScore(customerId: String, riskScore: Int) {
        viewModelScope.launch {
            try {
                customerRepository.updateRiskScore(customerId, riskScore)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun getCustomerByPhone(phone: String) {
        viewModelScope.launch {
            try {
                val customer = customerRepository.getCustomerByPhone(phone)
                if (customer != null) {
                    selectCustomer(customer)
                } else {
                    _errorMessage.value = "Customer not found for phone: $phone"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun getCustomerByEmail(email: String) {
        viewModelScope.launch {
            try {
                val customer = customerRepository.getCustomerByEmail(email)
                if (customer != null) {
                    selectCustomer(customer)
                } else {
                    _errorMessage.value = "Customer not found for email: $email"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun getCustomersWithLoyaltyPoints() {
        viewModelScope.launch {
            try {
                customerRepository.getCustomersWithLoyaltyPoints().collect { customers ->
                    _filteredCustomers.value = customers
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun getCustomersWithBalance() {
        viewModelScope.launch {
            try {
                customerRepository.getCustomersWithBalance().collect { customers ->
                    _filteredCustomers.value = customers
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun getHighRiskCustomers(threshold: Int = 70) {
        viewModelScope.launch {
            try {
                customerRepository.getHighRiskCustomers(threshold).collect { customers ->
                    _filteredCustomers.value = customers
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Helper function to create a new customer
    fun createNewCustomer(
        firstName: String,
        lastName: String,
        email: String? = null,
        phone: String? = null,
        address: String? = null,
        city: String? = null,
        state: String? = null,
        zipCode: String? = null,
        country: String? = null
    ): Customer {
        return Customer(
            id = UUID.randomUUID().toString(),
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone,
            address = address,
            city = city,
            state = state,
            zipCode = zipCode,
            country = country,
            loyaltyPoints = 0,
            totalSpent = BigDecimal.ZERO,
            balance = BigDecimal.ZERO,
            isActive = true,
            riskScore = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}
