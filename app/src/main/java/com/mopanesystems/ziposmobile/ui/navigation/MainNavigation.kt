package com.mopanesystems.ziposmobile.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mopanesystems.ziposmobile.ui.analytics.AnalyticsScreenCompose
import com.mopanesystems.ziposmobile.ui.customers.CustomersScreenCompose
import com.mopanesystems.ziposmobile.ui.inventory.InventoryScreenCompose
import com.mopanesystems.ziposmobile.ui.pos.POSScreenCompose
import com.mopanesystems.ziposmobile.ui.products.ProductsScreenCompose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(navController: androidx.navigation.NavController) {
    val navController = rememberNavController()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ziPOS Mobile") },
                actions = {
                    IconButton(onClick = { /* Settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "pos",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("pos") {
                POSScreenCompose(
                    viewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
                    onScanBarcode = { /* Handle barcode scanning */ },
                    onProcessPayment = { /* Handle payment */ },
                    onClearCart = { /* Handle clear cart */ },
                    currencyFormatter = java.text.NumberFormat.getCurrencyInstance()
                )
            }
            
            composable("products") {
                ProductsScreenCompose(
                    viewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                )
            }
            
            composable("customers") {
                CustomersScreenCompose(
                    viewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                )
            }
            
            composable("inventory") {
                InventoryScreenCompose(
                    viewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                )
            }
            
            composable("analytics") {
                AnalyticsScreenCompose(
                    viewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: androidx.navigation.NavController) {
    val items = listOf(
        BottomNavItem("pos", "POS", Icons.Default.PointOfSale),
        BottomNavItem("products", "Products", Icons.Default.Inventory),
        BottomNavItem("customers", "Customers", Icons.Default.People),
        BottomNavItem("inventory", "Inventory", Icons.Default.Storage),
        BottomNavItem("analytics", "Analytics", Icons.Default.Analytics)
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)
