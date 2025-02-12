package com.healthtracking.app.composables.graphs.generic

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()


@Composable
internal fun DatedBarChart(
    modifier: Modifier,
    stepSizeY: Float = 1f,
    maxYValue: Double? = null,
    data: Map<LocalDate, Float>,
    startAxisTitle: String? = null,
) {
    val hoursSlept = data.values.toList().ifEmpty { listOf(0) }
    val dates = data.keys.toList().ifEmpty { listOf(LocalDate.now()) }

    val modelProducer = remember { CartesianChartModelProducer() }
    val bottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
        context.model.extraStore[BottomAxisLabelKey][x.toInt()]
    }

    val rangeProvider = object : CartesianLayerRangeProvider
    {
        override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
            0.0

        override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
            maxYValue ?: (stepSizeY * ceil((maxY) / stepSizeY)) // round up to the next step
    }

    LaunchedEffect(data) {
        modelProducer.runTransaction {
            columnSeries {
                series(hoursSlept)
                extras { it[BottomAxisLabelKey] = dates.map { localDate -> localDate.format(
                    DateTimeFormatter.ofPattern("d MMM yy")) }
                }
            }
        }
    }

    CartesianChartHost(
        modifier = modifier,
        chart =
        rememberCartesianChart(
            layers = arrayOf(rememberColumnCartesianLayer(
                columnCollectionSpacing = 40.dp,
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                    vicoTheme.columnCartesianLayerColors.map {
                        rememberLineComponent(
                            fill = fill(MaterialTheme.colorScheme.tertiary),
                            thickness = 30.dp
                        )
                    }
                ),
                rangeProvider = rangeProvider
            )),
            startAxis = VerticalAxis.rememberStart(
                title = startAxisTitle,
                titleComponent = TextComponent(
                    color = MaterialTheme.colorScheme.onSurface.toArgb(),
                    textSizeSp = 15f
                ),
                itemPlacer = VerticalAxis.ItemPlacer.step(step = { stepSizeY.toDouble() })
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                line = rememberAxisLineComponent(fill(color = MaterialTheme.colorScheme.onBackground)),
                label = TextComponent(textSizeSp = 12f, color = MaterialTheme.colorScheme.onSurface.toArgb()),
                valueFormatter = bottomAxisValueFormatter,
            ),
        ),
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End),
    )
}