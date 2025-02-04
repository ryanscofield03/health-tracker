package com.healthtracking.app.composables.screens.eat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.xr.runtime.math.toRadians
import com.healthtracking.app.ui.theme.CarbsColour
import com.healthtracking.app.ui.theme.FatsColour
import com.healthtracking.app.ui.theme.ProteinColour
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(slice.color, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${slice.value.toInt()}g",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Slider(
                value = slice.value,
                onValueChange = { newValue ->
                    when (slice.label) {
                        "Protein" -> updateProtein(newValue)
                        "Carbs" -> updateCarbs(newValue)
                        "Fats" -> updateFats(newValue)
                    }
                },
                colors = SliderDefaults.colors(
                    thumbColor = slice.color,
                    activeTrackColor = slice.color,
                    inactiveTrackColor = slice.color.copy(alpha = 0.2f)
                ),
                valueRange = 0f..slice.maxValue,
                modifier = Modifier.fillMaxWidth()
            )
        }
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