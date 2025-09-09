package com.mopanesystems.ziposmobile.ui.pos

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mopanesystems.ziposmobile.data.model.PaymentMethod
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedPOSScreenCompose(
    viewModel: POSViewModel,
    onScanBarcode: () -> Unit,
    onProcessPayment: (PaymentMethod) -> Unit,
    onClearCart: () -> Unit,
    currencyFormatter: NumberFormat
) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val subtotal by viewModel.subtotal.collectAsStateWithLifecycle()
    val taxAmount by viewModel.taxAmount.collectAsStateWithLifecycle()
    val totalAmount by viewModel.totalAmount.collectAsStateWithLifecycle()
    val itemCount by viewModel.itemCount.collectAsStateWithLifecycle()
    val selectedCustomer by viewModel.selectedCustomer.collectAsStateWithLifecycle()
    val isProcessing by viewModel.isProcessing.collectAsStateWithLifecycle()
    
    // Animation states
    var showCart by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (showCart) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    LaunchedEffect(cartItems.isNotEmpty()) {
        showCart = cartItems.isNotEmpty()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Animated Header
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Point of Sale",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                // Animated Scan Button
                val scanButtonScale by animateFloatAsState(
                    targetValue = if (isProcessing) 0.9f else 1f,
                    animationSpec = tween(200)
                )
                
                IconButton(
                    onClick = onScanBarcode,
                    modifier = Modifier.scale(scanButtonScale)
                ) {
                    Icon(
                        Icons.Default.QrCodeScanner, 
                        contentDescription = "Scan Barcode",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Animated Customer Info
        AnimatedVisibility(
            visible = selectedCustomer != null,
            enter = slideInHorizontally() + fadeIn(),
            exit = slideOutHorizontally() + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scale)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Customer:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = selectedCustomer?.fullName ?: "No Customer Selected",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(onClick = { /* Add Customer */ }) {
                        Text("Add Customer")
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Animated Cart Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .scale(scale)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cart Items (${itemCount})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Animated item count badge
                    AnimatedVisibility(
                        visible = itemCount > 0,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = itemCount.toString(),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (cartItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No items in cart",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn {
                        items(
                            items = cartItems,
                            key = { it.id }
                        ) { item ->
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically() + fadeIn(),
                                exit = slideOutVertically() + fadeOut()
                            ) {
                                CartItemRowCompose(
                                    item = item,
                                    onQuantityChange = { newQuantity ->
                                        // Update quantity in ViewModel
                                    },
                                    onRemove = {
                                        viewModel.removeProductFromCart(item.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Animated Summary and Actions
        AnimatedVisibility(
            visible = cartItems.isNotEmpty(),
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Animated Summary
                    AnimatedContent(
                        targetState = totalAmount,
                        transitionSpec = {
                            slideInVertically() + fadeIn() togetherWith
                            slideOutVertically() + fadeOut()
                        }
                    ) { total ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Subtotal:",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = currencyFormatter.format(subtotal),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Tax:",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = currencyFormatter.format(taxAmount),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total:",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = currencyFormatter.format(total),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Animated Payment Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val paymentMethods = listOf(
                            PaymentMethod.CASH to "Cash",
                            PaymentMethod.CARD to "Card",
                            PaymentMethod.MOBILE_PAYMENT to "Mobile"
                        )
                        
                        paymentMethods.forEach { (method, label) ->
                            val buttonScale by animateFloatAsState(
                                targetValue = if (isProcessing) 0.95f else 1f,
                                animationSpec = tween(100)
                            )
                            
                            Button(
                                onClick = { onProcessPayment(method) },
                                modifier = Modifier
                                    .weight(1f)
                                    .scale(buttonScale),
                                enabled = cartItems.isNotEmpty() && !isProcessing
                            ) {
                                Text(label)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedButton(
                        onClick = onClearCart,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Clear Cart")
                    }
                    
                    // Animated Progress Indicator
                    AnimatedVisibility(
                        visible = isProcessing,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
