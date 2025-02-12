package com.healthtracking.app.composables.screens.eat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.xr.runtime.math.toRadians
import com.healthtracking.app.composables.SliderWithLabel
import com.healthtracking.app.theme.CarbsColour
import com.healthtracking.app.theme.FatsColour
import com.healthtracking.app.theme.ProteinColour
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun MacroPieChart(
    modifier: Modifier = Modifier,
    protein: Float,
    carbs: Float,
    fats: Float,
    updateProtein: (Float) -> Unit,
    updateCarbs: (Float) -> Unit,
    updateFats: (Float) -> Unit,
    maxProtein: Float,
    maxCarbs: Float,
    maxFats: Float
) {
    val data = listOf(
        PieChartData(protein, ProteinColour, "Protein", maxProtein),
        PieChartData(carbs, CarbsColour, "Carbs", maxCarbs),
        PieChartData(fats, FatsColour, "Fats", maxFats)
    )

    // Pie chart
    Box(
        modifier = modifier
            .size(200.dp)
            .padding(16.dp)
    ) {
        PieChart(
            data = data
        )
    }

    Spacer(modifier = Modifier.width(16.dp))

    // Macro Labels and slider
    data.forEach { slice ->
        SliderWithLabel(
            label = "${slice.label}: ${slice.value.toInt()}g",
            value = slice.value,
            color = slice.color,
            maxSliderValue = slice.maxValue,
            onValueChange = { newValue ->
                when (slice.label) {
                    "Protein" -> updateProtein(newValue)
                    "Carbs" -> updateCarbs(newValue)
                    "Fats" -> updateFats(newValue)
                }
            }
        )
    }
}

@Composable
private fun PieChart(
    data: List<PieChartData>,
) {
    val total = data.sumOf { it.value.toDouble() }.toFloat()
    val sweepAngles = data.map { it.value / total * 360f }

    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()

    Canvas(
        modifier = Modifier.size(160.dp)) {
        var startAngle = 0f
        data.forEachIndexed { index, slice ->
            drawArc(
                color = slice.color,
                startAngle = startAngle,
                sweepAngle = sweepAngles[index],
                useCenter = true
            )

            val angle = startAngle + sweepAngles[index] / 2
            val labelRadius = size.minDimension / 1.6
            val labelX = (size.width / 2) + labelRadius * cos(toRadians(angle))
            val labelY = (size.height / 2) + labelRadius * sin(toRadians(angle))

            // Draw the label
            drawContext.canvas.nativeCanvas.drawText(
                slice.label,
                labelX.toFloat(),
                labelY.toFloat(),
                android.graphics.Paint().apply {
                    color = textColor
                    textSize = 30f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )

            startAngle += sweepAngles[index]
        }
    }
}