package com.healthtracking.app.composables.screens.eat

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.healthtracking.app.graph.rememberMarker
import com.healthtracking.app.ui.theme.CaloriesColour
import com.healthtracking.app.ui.theme.CarbsColour
import com.healthtracking.app.ui.theme.FatsColour
import com.healthtracking.app.ui.theme.ProteinColour
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore

private val RangeProvider = CartesianLayerRangeProvider.fixed(maxY = 100.0)

private val YDecimalFormat = java.text.DecimalFormat("#.##'%'")

private val StartAxisItemPlacer = VerticalAxis.ItemPlacer.step(step = { 25.0 })

private val StartAxisValueFormatter = CartesianValueFormatter { _, value, _ ->
    YDecimalFormat.format(value)
}

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()

private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    println(x)
    println(context.model.extraStore[BottomAxisLabelKey][x.toInt()])
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
}

private val MarkerValueFormatter =
    DefaultCartesianMarker.ValueFormatter { _, targets ->
        val column = (targets[0] as ColumnCartesianLayerMarkerTarget).columns[0]
        SpannableStringBuilder()
            .append(
                YDecimalFormat.format(column.entry.y),
                ForegroundColorSpan(column.color),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
    }

@Composable
fun BarChart(
    caloriesProgressPercent: Long,
    proteinProgressPercent: Long,
    carbsProgressPercent: Long,
    fatsProgressPercent: Long
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val data = mapOf(
        "Kcal" to caloriesProgressPercent,
        "P" to proteinProgressPercent,
        "C" to carbsProgressPercent,
        "F" to fatsProgressPercent
    )

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries { series(data.values) }
            extras { it[BottomAxisLabelKey] = data.keys.toList() }
        }
    }

    val columnMap = mapOf(
        "Calories" to rememberLineComponent(
            fill = fill(CaloriesColour),
            thickness = 12.dp
        ),
        "Protein" to rememberLineComponent(
            fill = fill(ProteinColour),
            thickness = 12.dp
        ),
        "Carbs" to rememberLineComponent(
            fill = fill(CarbsColour),
            thickness = 12.dp
        ),
        "Fats" to rememberLineComponent(
            fill = fill(FatsColour),
            thickness = 12.dp
        ),
    )

    CartesianChartHost(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = remember(columnMap) {
                    getWeeklyFoodColumnProvider(columnMap)
                },
                rangeProvider = RangeProvider
            ),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = StartAxisValueFormatter,
                itemPlacer = StartAxisItemPlacer,
                line = rememberAxisLineComponent(
                    fill(color = MaterialTheme.colorScheme.onBackground)
                ),
                guideline = rememberAxisGuidelineComponent(
                    fill(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                ),
                // only accepts Int values for colour rather than Color objects
                label = TextComponent(textSizeSp = 10f, color = if (isSystemInDarkTheme()) Color.WHITE else Color.BLACK),
                labelRotationDegrees = -25f,
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                label = TextComponent(textSizeSp = 12f, color = if (isSystemInDarkTheme()) Color.WHITE else Color.BLACK),
                valueFormatter = BottomAxisValueFormatter,
                line = rememberAxisLineComponent(
                    fill(color = MaterialTheme.colorScheme.onBackground)
                ),
            ),
            layerPadding = { cartesianLayerPadding(scalableStart = 4.dp, scalableEnd = 4.dp) },
            marker = rememberMarker(MarkerValueFormatter)
        ),
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(scrollEnabled = false),
    )
}

private fun getWeeklyFoodColumnProvider(columnMap: Map<String, LineComponent>) =
    object : ColumnCartesianLayer.ColumnProvider {
        override fun getColumn(
            entry: ColumnCartesianLayerModel.Entry,
            seriesIndex: Int,
            extraStore: ExtraStore,
        ) = when (entry.x) {
            0.0 -> columnMap.getValue("Calories")
            1.0 -> columnMap.getValue("Protein")
            2.0 -> columnMap.getValue("Carbs")
            else -> columnMap.getValue("Fats")
        }

        override fun getWidestSeriesColumn(
            seriesIndex: Int,
            extraStore: ExtraStore
        ): LineComponent {
            return columnMap.getValue("Calories")
        }

    }