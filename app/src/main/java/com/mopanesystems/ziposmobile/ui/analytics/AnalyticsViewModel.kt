package com.mopanesystems.ziposmobile.ui.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.mopanesystems.ziposmobile.data.database.POSDatabase
import com.mopanesystems.ziposmobile.data.model.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class AnalyticsViewModel(private val database: POSDatabase) : ViewModel() {

    private val _dailySales = MutableLiveData<List<DailySales>>()
    val dailySales: LiveData<List<DailySales>> = _dailySales

    private val _topProducts = MutableLiveData<List<ProductPerformance>>()
    val topProducts: LiveData<List<ProductPerformance>> = _topProducts

    private val _customerAnalytics = MutableLiveData<List<CustomerAnalytics>>()
    val customerAnalytics: LiveData<List<CustomerAnalytics>> = _customerAnalytics

    private val _totalSales = MutableLiveData<BigDecimal>()
    val totalSales: LiveData<BigDecimal> = _totalSales

    private val _transactionCount = MutableLiveData<Int>()
    val transactionCount: LiveData<Int> = _transactionCount

    private val _averageTransaction = MutableLiveData<BigDecimal>()
    val averageTransaction: LiveData<BigDecimal> = _averageTransaction

    private val _totalRefunds = MutableLiveData<BigDecimal>()
    val totalRefunds: LiveData<BigDecimal> = _totalRefunds

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var currentPeriod = "daily"

    init {
        loadAnalyticsData()
    }

    fun setPeriod(period: String) {
        currentPeriod = period
        loadAnalyticsData()
    }

    private fun loadAnalyticsData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                val endDate = LocalDateTime.now()
                val startDate = when (currentPeriod) {
                    "daily" -> endDate.truncatedTo(ChronoUnit.DAYS)
                    "weekly" -> endDate.minusWeeks(1)
                    "monthly" -> endDate.minusMonths(1)
                    else -> endDate.truncatedTo(ChronoUnit.DAYS)
                }

                // Load daily sales data
                val salesData = database.dailySalesDao().getDailySalesByDateRange(startDate, endDate)
                salesData.collect { salesList ->
                    _dailySales.value = salesList
                    updateSummaryStats(salesList)
                }

                // Load top products
                val productsData = database.productPerformanceDao().getProductPerformanceByPeriod(
                    currentPeriod, startDate, endDate
                )
                productsData.collect { productsList ->
                    _topProducts.value = productsList.take(10) // Top 10 products
                }

                // Load customer analytics
                val customersData = database.customerAnalyticsDao().getCustomerAnalyticsByPeriod(
                    currentPeriod, startDate, endDate
                )
                customersData.collect { customersList ->
                    _customerAnalytics.value = customersList.take(10) // Top 10 customers
                }

            } catch (e: Exception) {
                _errorMessage.value = "Error loading analytics data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun updateSummaryStats(salesList: List<DailySales>) {
        val totalSalesAmount = salesList.sumOf { it.totalSales }
        val totalTransactions = salesList.sumOf { it.totalTransactions }
        val totalRefundsAmount = salesList.sumOf { it.refunds }
        
        _totalSales.value = totalSalesAmount
        _transactionCount.value = totalTransactions
        _totalRefunds.value = totalRefundsAmount
        
        val avgTransaction = if (totalTransactions > 0) {
            totalSalesAmount.divide(BigDecimal(totalTransactions), 2, java.math.RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO
        }
        _averageTransaction.value = avgTransaction
    }

    fun createSalesTrendChartData(): LineData? {
        val salesList = _dailySales.value ?: return null
        
        val entries = mutableListOf<Entry>()
        salesList.forEachIndexed { index, dailySales ->
            entries.add(Entry(index.toFloat(), dailySales.totalSales.toFloat()))
        }

        val dataSet = LineDataSet(entries, "Sales Trend").apply {
            color = android.graphics.Color.parseColor("#4CAF50")
            valueTextColor = android.graphics.Color.BLACK
            lineWidth = 2f
            setCircleColor(android.graphics.Color.parseColor("#4CAF50"))
            setCircleRadius(4f)
            setDrawValues(false)
        }

        return LineData(dataSet)
    }

    fun refreshData() {
        loadAnalyticsData()
    }
}
