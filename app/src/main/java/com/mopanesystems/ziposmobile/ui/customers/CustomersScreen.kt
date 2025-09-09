package com.mopanesystems.ziposmobile.ui.customers

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var customers by remember { mutableStateOf(listOf<Customer>()) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersScreenCompose(
    viewModel: CustomerViewModel
) {
    val filteredCustomers by viewModel.filteredCustomers.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val selectedCustomer by viewModel.selectedCustomer.collectAsStateWithLifecycle()
    
    var searchQuery by remember { mutableStateOf("") }
    var searchType by remember { mutableStateOf("name") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Customers",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* Add Customer */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Customer")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search customers...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Search Type Selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                onClick = { searchType = "name" },
                label = { Text("Name") },
                selected = searchType == "name"
            )
            FilterChip(
                onClick = { searchType = "phone" },
                label = { Text("Phone") },
                selected = searchType == "phone"
            )
            FilterChip(
                onClick = { searchType = "email" },
                label = { Text("Email") },
                selected = searchType == "email"
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Filter Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.getCustomersWithLoyaltyPoints() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Loyalty")
            }
            Button(
                onClick = { viewModel.getCustomersWithBalance() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Balance")
            }
            Button(
                onClick = { viewModel.getHighRiskCustomers() },
                modifier = Modifier.weight(1f)
            ) {
                Text("High Risk")
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedButton(
            onClick = { viewModel.clearFilters() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear Filters")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Selected Customer Info
        selectedCustomer?.let { customer ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Selected Customer",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Name: ${customer.fullName}")
                    Text(text = "Phone: ${customer.phone ?: "No phone"}")
                    Text(text = "Email: ${customer.email ?: "No email"}")
                    Text(text = "Balance: $${customer.balance}")
                    Text(text = "Loyalty Points: ${customer.loyaltyPoints}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Customers List
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (filteredCustomers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No customers found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredCustomers) { customer ->
                    CustomerCardCompose(
                        customer = customer,
                        onEdit = { /* Edit Customer */ },
                        onDelete = { /* Delete Customer */ },
                        onSelect = { viewModel.selectCustomer(customer) }
                    )
                }
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
fun CustomerCard(
    customer: Customer,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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
                    text = customer.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = customer.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Phone: ${customer.phone}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Balance: $${String.format("%.2f", customer.balance)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (customer.balance > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
fun CustomerCardCompose(
    customer: com.mopanesystems.ziposmobile.data.model.Customer,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSelect: () -> Unit
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
                    text = customer.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = customer.email ?: "No email",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Phone: ${customer.phone ?: "No phone"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Balance: $${customer.balance}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (customer.balance > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Loyalty Points: ${customer.loyaltyPoints}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onSelect) {
                    Icon(Icons.Default.Person, contentDescription = "Select")
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

data class Customer(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val balance: Double
)
