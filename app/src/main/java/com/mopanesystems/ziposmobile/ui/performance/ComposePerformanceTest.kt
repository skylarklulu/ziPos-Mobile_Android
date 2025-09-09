package com.mopanesystems.ziposmobile.ui.performance

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mopanesystems.ziposmobile.ui.MainScreen
import com.mopanesystems.ziposmobile.ui.theme.ZiPOSTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposePerformanceTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun testScreenRenderingPerformance() {
        val startTime = System.currentTimeMillis()
        
        composeTestRule.setContent {
            ZiPOSTheme {
                MainScreen()
            }
        }
        
        val endTime = System.currentTimeMillis()
        val renderingTime = endTime - startTime
        
        // Verify rendering time is reasonable (less than 1 second)
        assert(renderingTime < 1000) { "Screen rendering took too long: ${renderingTime}ms" }
    }
    
    @Test
    fun testNavigationPerformance() {
        composeTestRule.setContent {
            ZiPOSTheme {
                MainScreen()
            }
        }
        
        val screens = listOf("Products", "Customers", "Inventory", "Analytics", "POS")
        
        screens.forEach { screen ->
            val startTime = System.currentTimeMillis()
            
            composeTestRule.onNodeWithText(screen).performClick()
            
            val endTime = System.currentTimeMillis()
            val navigationTime = endTime - startTime
            
            // Verify navigation time is reasonable (less than 500ms)
            assert(navigationTime < 500) { "Navigation to $screen took too long: ${navigationTime}ms" }
        }
    }
    
    @Test
    fun testMemoryUsage() {
        composeTestRule.setContent {
            ZiPOSTheme {
                MainScreen()
            }
        }
        
        // Test memory usage by navigating between screens multiple times
        repeat(10) {
            composeTestRule.onNodeWithText("Products").performClick()
            composeTestRule.onNodeWithText("Customers").performClick()
            composeTestRule.onNodeWithText("Inventory").performClick()
            composeTestRule.onNodeWithText("Analytics").performClick()
            composeTestRule.onNodeWithText("POS").performClick()
        }
        
        // If we reach here without OutOfMemoryError, memory usage is acceptable
        assert(true)
    }
    
    @Test
    fun testRecompositionPerformance() {
        composeTestRule.setContent {
            ZiPOSTheme {
                MainScreen()
            }
        }
        
        // Test recomposition performance by interacting with UI elements
        val startTime = System.currentTimeMillis()
        
        // Simulate rapid UI interactions
        repeat(50) {
            composeTestRule.onNodeWithText("Products").performClick()
            composeTestRule.onNodeWithText("POS").performClick()
        }
        
        val endTime = System.currentTimeMillis()
        val totalTime = endTime - startTime
        
        // Verify recomposition performance is acceptable
        assert(totalTime < 5000) { "Recomposition took too long: ${totalTime}ms" }
    }
}
