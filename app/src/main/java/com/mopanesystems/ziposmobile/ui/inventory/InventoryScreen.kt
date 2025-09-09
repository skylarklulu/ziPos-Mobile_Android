package com.mopanesystems.ziposmobile.ui.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mopanesystems.ziposmobile.ui.inventory.InventoryViewModel
import com.mopanesystems.ziposmobile.data.model.StockAlert
import com.mopanesystems.ziposmobile.data.model.InventoryAdjustment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel = hiltViewModel()
) {
    InventoryScreenCompose(viewModel = viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenCompose(
    viewModel: InventoryViewModel
) {
    val stockAlerts by viewModel.stockAlerts.observeAsState(emptyList())
    val recentAdjustments by viewModel.recentAdjustments.observeAsState(emptyList())
    val lowStockCount by viewModel.lowStockCount.observeAsState(0)
    val outOfStockCount by viewModel.outOfStockCount.observeAsState(0)
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Inventory Management",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Quick Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                title = "Low Stock",
                count = lowStockCount,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Out of Stock",
                count = outOfStockCount,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* Stock Adjustment */ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Stock Adjustment")
            }
            
            Button(
                onClick = { /* Purchase Order */ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Purchase Order")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Stock Alerts
        Text(
            text = "Stock Alerts",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(stockAlerts) { alert ->
                StockAlertCardCompose(
                    alert = alert,
                    onAcknowledge = { viewModel.acknowledgeAlert(alert) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Recent Adjustments
        Text(
            text = "Recent Adjustments",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recentAdjustments) { adjustment ->
                AdjustmentCardCompose(adjustment = adjustment)
            }
        }
        
        // Error message
        errorMessage?.let { message ->
            LaunchedEffect(message) {
                // Show error message
            }
        }
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineLarge,
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
fun StockAlertCard(
    alert: StockAlert,
    onAcknowledge: () -> Unit
) {
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
                    text = alert.productId, // TODO: Fetch product name from Product entity
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Current: ${alert.currentStock} | Threshold: ${alert.threshold}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = alert.alertType.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            Button(
                onClick = onAcknowledge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Acknowledge")
            }
        }
    }
}

@Composable
fun AdjustmentCard(adjustment: InventoryAdjustment) {
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
                    text = adjustment.productId, // TODO: Fetch product name from Product entity
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Quantity: ${adjustment.quantity} | Reason: ${adjustment.reason}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "By: ${adjustment.userId} | ${adjustment.createdAt}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// StockAlert data class is defined in data.model package

@Composable
fun StockAlertCardCompose(
    alert: com.mopanesystems.ziposmobile.data.model.StockAlert,
    onAcknowledge: () -> Unit
) {
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
                    text = alert.productId, // TODO: Fetch product name from Product entity
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Current: ${alert.currentStock} | Threshold: ${alert.threshold}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = alert.alertType.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            Button(
                onClick = onAcknowledge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Acknowledge")
            }
        }
    }
}

@Composable
fun AdjustmentCardCompose(adjustment: com.mopanesystems.ziposmobile.data.model.InventoryAdjustment) {
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
                    text = adjustment.productId, // TODO: Fetch product name from Product entity
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Quantity: ${adjustment.quantity} | Reason: ${adjustment.reason.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "By: ${adjustment.userId} | ${adjustment.createdAt}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// InventoryAdjustment data class is defined in data.model package
