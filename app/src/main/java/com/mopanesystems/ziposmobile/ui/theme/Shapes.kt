package com.mopanesystems.ziposmobile.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Modern Material Design 3 Shape System
val Shapes = Shapes(
    // Extra small shapes for chips, buttons, etc.
    extraSmall = RoundedCornerShape(4.dp),
    
    // Small shapes for cards, smaller elements
    small = RoundedCornerShape(8.dp),
    
    // Medium shapes for dialogs, bottom sheets
    medium = RoundedCornerShape(12.dp),
    
    // Large shapes for prominent surfaces
    large = RoundedCornerShape(16.dp),
    
    // Extra large shapes for largest containers
    extraLarge = RoundedCornerShape(24.dp)
)
