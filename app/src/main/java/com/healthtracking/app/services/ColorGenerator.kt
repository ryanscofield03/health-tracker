package com.healthtracking.app.services

import android.graphics.Color
import androidx.compose.ui.graphics.Color as ComposeColor

/**
 * Generates a color for a bar based on its index and the total number of bars.
 */
fun generateColor(index: Int, numColors: Int): ComposeColor {
    val hue = (360f / numColors) * index
    val saturation = 0.6f
    val lightness = 0.8f
    return ComposeColor(Color.HSVToColor(listOf(hue, saturation, lightness).toFloatArray()))
}