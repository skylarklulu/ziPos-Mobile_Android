package com.mopanesystems.ziposmobile.ui.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen() {
    var selectedPeriod by remember { mutableStateOf("Today") }
    var totalSales by remember { mutableStateOf(0.0) }
    var transactionCount by remember { mutableStateOf(0) }
    var averageTransaction by remember { mutableStateOf(0.0) }
    var topProducts by remember { mutableStateOf(listOf<TopProduct>()) }
    var customerAnalytics by remember { mutableStateOf(listOf<CustomerAnalytics>()) }
    
    val periods = listOf("Today", "This Week", "This Month", "This Year")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreenCompose(
    viewModel: AnalyticsViewModel
) {
    val dailySales by viewModel.dailySales.observeAsState(emptyList())
    val topProducts by viewModel.topProducts.observeAsState(emptyList())
    val customerAnalytics by viewModel.customerAnalytics.observeAsState(emptyList())
    val totalSales by viewModel.totalSales.observeAsState(BigDecimal.ZERO)
    val transactionCount by viewModel.transactionCount.observeAsState(0)
    val averageTransaction by viewModel.averageTransaction.observeAsState(BigDecimal.ZERO)
    val totalRefunds by viewModel.totalRefunds.observeAsState(BigDecimal.ZERO)
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    
    var selectedPeriod by remember { mutableStateOf("Today") }
    val periods = listOf("Today", "This Week", "This Month", "This Year")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Analytics & Reporting",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Period Selection
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(periods) { period ->
                FilterChip(
                    onClick = { 
                        selectedPeriod = period
                        when (period) {
                            "Today" -> viewModel.setPeriod("daily")
                            "This Week" -> viewModel.setPeriod("weekly")
                            "This Month" -> viewModel.setPeriod("monthly")
                            "This Year" -> viewModel.setPeriod("yearly")
                        }
                    },
                    label = { Text(period) },
                    selected = selectedPeriod == period
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Sales Summary
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Sales Summary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricCard(
                        title = "Total Sales",
                        value = formatCurrency(totalSales),
                        color = MaterialTheme.colorScheme.primary
                    )
                    MetricCard(
                        title = "Transactions",
                        value = transactionCount.toString(),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricCard(
                        title = "Avg Transaction",
                        value = formatCurrency(averageTransaction),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    MetricCard(
                        title = "Refunds",
                        value = formatCurrency(totalRefunds),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Top Products
        Text(
            text = "Top Products",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(topProducts) { product ->
                    TopProductCardCompose(product = product)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Customer Analytics
        Text(
            text = "Customer Insights",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(customerAnalytics) { customer ->
                CustomerAnalyticsCardCompose(customer = customer)
            }
        }
        
        // Error message
        errorMessage?.let { message ->
            LaunchedEffect(message) {
                // Show error message
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.weight(1f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TopProductCard(product: TopProduct) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Quantity: ${product.quantitySold}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Text(
                text = "$${String.format("%.2f", product.revenue)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun CustomerAnalyticsCard(customer: CustomerAnalytics) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = customer.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Transactions: ${customer.transactionCount}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Tier: ${customer.loyaltyTier}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                text = "$${String.format("%.2f", customer.totalSpent)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

data class TopProduct(
    val id: String,
    val name: String,
    val quantitySold: Int,
    val revenue: Double
)

@Composable
fun TopProductCardCompose(product: com.mopanesystems.ziposmobile.data.model.ProductPerformance) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Quantity: ${product.quantitySold}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Text(
                text = formatCurrency(product.revenue),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun CustomerAnalyticsCardCompose(customer: com.mopanesystems.ziposmobile.data.model.CustomerAnalytics) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = customer.customerName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Transactions: ${customer.transactionCount}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Tier: ${customer.loyaltyTier}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                text = formatCurrency(customer.totalSpent),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun formatCurrency(amount: BigDecimal): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    return formatter.format(amount)
}

data class CustomerAnalytics(
    val id: String,
    val name: String,
    val totalSpent: Double,
    val transactionCount: Int,
    val loyaltyTier: String
)
