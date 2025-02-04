package com.healthtracking.app.composables.screens.eat

import androidx.compose.ui.graphics.Color

internal data class PieChartData(
    val value: Float,
    val color: Color,
    val label: String,
    val maxValue: Float
)