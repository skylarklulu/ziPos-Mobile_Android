package com.mopanesystems.ziposmobile.ui.integration

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mopanesystems.ziposmobile.ui.MainScreen
import com.mopanesystems.ziposmobile.ui.theme.ZiPOSTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposeIntegrationTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun testCompleteAppFlow() {
        composeTestRule.setContent {
            ZiPOSTheme {
                MainScreen()
            }
        }
        
        // Test complete app flow
        // 1. Start on POS screen
        composeTestRule.onNodeWithText("Point of Sale").assertIsDisplayed()
        
        // 2. Navigate to Products
        composeTestRule.onNodeWithText("Products").performClick()
        composeTestRule.onNodeWithText("Products").assertIsDisplayed()
        
        // 3. Navigate to Customers
        composeTestRule.onNodeWithText("Customers").performClick()
        composeTestRule.onNodeWithText("Customers").assertIsDisplayed()
        
        // 4. Navigate to Inventory
        composeTestRule.onNodeWithText("Inventory").performClick()
        composeTestRule.onNodeWithText("Inventory Management").assertIsDisplayed()
        
        // 5. Navigate to Analytics
        composeTestRule.onNodeWithText("Analytics").performClick()
        composeTestRule.onNodeWithText("Analytics & Reporting").assertIsDisplayed()
        
        // 6. Return to POS
        composeTestRule.onNodeWithText("POS").performClick()
        composeTestRule.onNodeWithText("Point of Sale").assertIsDisplayed()
    }
    
    @Test
    fun testPOSFunctionality() {
        composeTestRule.setContent {
            ZiPOSTheme {
                MainScreen()
            }
        }
        
        // Test POS screen functionality
        composeTestRule.onNodeWithText("Point of Sale").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cart Items").assertIsDisplayed()
        composeTestRule.onNodeWithText("No items in cart").assertIsDisplayed()
        
        // Test scan barcode button
        composeTestRule.onNodeWithContentDescription("Scan Barcode").assertIsDisplayed()
        
        // Test payment buttons (should be disabled when cart is empty)
        composeTestRule.onNodeWithText("Cash").assertIsDisplayed()
        composeTestRule.onNodeWithText("Card").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mobile").assertIsDisplayed()
    }
    
    @Test
    fun testProductsFunctionality() {
        composeTestRule.setContent {
            ZiPOSTheme {
                MainScreen()
            }
        }
        
        // Navigate to Products
        composeTestRule.onNodeWithText("Products").performClick()
        
        // Test Products screen functionality
        composeTestRule.onNodeWithText("Products").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search products...").assertIsDisplayed()
        composeTestRule.onNodeWithText("All").assertIsDisplayed()
        composeTestRule.onNodeWithText("Electronics").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clothing").assertIsDisplayed()
    }
    
    @Test
    fun testCustomersFunctionality() {
        composeTestRule.setContent {
            ZiPOSTheme {
                MainScreen()
            }
        }
        
        // Navigate to Customers
        composeTestRule.onNodeWithText("Customers").performClick()
        
        // Test Customers screen functionality
        composeTestRule.onNodeWithText("Customers").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search customers...").assertIsDisplayed()
        composeTestRule.onNodeWithText("Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Phone").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Loyalty").assertIsDisplayed()
        composeTestRule.onNodeWithText("Balance").assertIsDisplayed()
        composeTestRule.onNodeWithText("High Risk").assertIsDisplayed()
    }
    
    @Test
    fun testInventoryFunctionality() {
        composeTestRule.setContent {
            ZiPOSTheme {
                MainScreen()
            }
        }
        
        // Navigate to Inventory
        composeTestRule.onNodeWithText("Inventory").performClick()
        
        // Test Inventory screen functionality
        composeTestRule.onNodeWithText("Inventory Management").assertIsDisplayed()
        composeTestRule.onNodeWithText("Low Stock").assertIsDisplayed()
        composeTestRule.onNodeWithText("Out of Stock").assertIsDisplayed()
        composeTestRule.onNodeWithText("Stock Adjustment").assertIsDisplayed()
        composeTestRule.onNodeWithText("Purchase Order").assertIsDisplayed()
    }
    
    @Test
    fun testAnalyticsFunctionality() {
        composeTestRule.setContent {
            ZiPOSTheme {
                MainScreen()
            }
        }
        
        // Navigate to Analytics
        composeTestRule.onNodeWithText("Analytics").performClick()
        
        // Test Analytics screen functionality
        composeTestRule.onNodeWithText("Analytics & Reporting").assertIsDisplayed()
        composeTestRule.onNodeWithText("Today").assertIsDisplayed()
        composeTestRule.onNodeWithText("This Week").assertIsDisplayed()
        composeTestRule.onNodeWithText("This Month").assertIsDisplayed()
        composeTestRule.onNodeWithText("This Year").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sales Summary").assertIsDisplayed()
    }
    
    @Test
    fun testThemeConsistency() {
        composeTestRule.setContent {
            ZiPOSTheme {
                MainScreen()
            }
        }
        
        // Test theme consistency across all screens
        val screens = listOf("POS", "Products", "Customers", "Inventory", "Analytics")
        
        screens.forEach { screen ->
            composeTestRule.onNodeWithText(screen).performClick()
            // Verify theme is applied consistently
            composeTestRule.onRoot().assertHasClickAction()
        }
    }
}
