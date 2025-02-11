package com.healthtracking.app.composables.graphs.generic

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.healthtracking.app.services.generateColor
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.max

private val LegendLabelKey = ExtraStore.Key<Set<String>>()
private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()

@Composable
internal fun MultiBarDatedBarChart(
    modifier: Modifier,
    stepSizeY: Double = 1.0,
    data: Map<LocalDate, List<Float>>,
    startAxisTitle: String,
) {
    val legendLabelComponent = rememberTextComponent(color = MaterialTheme.colorScheme.onSurface)
    val dates = data.keys.toList().ifEmpty { listOf(LocalDate.now()) }

    val modelProducer = remember { CartesianChartModelProducer() }
    val bottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
        context.model.extraStore[BottomAxisLabelKey][x.toInt()]
    }

    val numBars = maxOf(data.values.ifEmpty { listOf(listOf(0f)) }.maxBy{ it.size }.size, 1)
    val columns = List(numBars) { index ->
        LineComponent(
            fill = fill(color = generateColor(index, numBars)),
            thicknessDp = 10f,
            shape = CorneredShape.rounded(topLeftPercent = 40, topRightPercent = 40)
        )
    }

    LaunchedEffect(data) {
        modelProducer.runTransaction {
            columnSeries {
                repeat(numBars) { barIndex ->
                    var seriesData = data.values.map { it.getOrNull(barIndex) ?: 0.0 }
                    if (seriesData.isEmpty()) seriesData = listOf(0)

                    series(seriesData)
                }
                extras {
                    it[BottomAxisLabelKey] = dates.map { localDate ->
                        localDate.format(
                            DateTimeFormatter.ofPattern("d MMM yy")
                        )
                    }

                    it[LegendLabelKey] = List(numBars) { index -> "Set ${index + 1}" }.toSet()
                }
            }
        }
    }

    CartesianChartHost(
        modifier = modifier,
        chart =
        rememberCartesianChart(
            layers = arrayOf(
                rememberColumnCartesianLayer(
                    columnCollectionSpacing = 20.dp,
                    columnProvider = remember(columns, data.values) {
                        getColumnProvider(columns = columns, values = data.values)
                    },
                )
            ),
            startAxis = VerticalAxis.rememberStart(
                title = startAxisTitle,
                titleComponent = TextComponent(
                    color = MaterialTheme.colorScheme.onSurface.toArgb(),
                    textSizeSp = 15f
                ),
                itemPlacer = VerticalAxis.ItemPlacer.step(step = { stepSizeY })
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                line = rememberAxisLineComponent(fill(color = MaterialTheme.colorScheme.onBackground)),
                label = TextComponent(textSizeSp = 12f, color = MaterialTheme.colorScheme.onSurface.toArgb()),
                valueFormatter = bottomAxisValueFormatter
            ),
            legend = rememberHorizontalLegend(
                items = { extraStore ->
                    extraStore[LegendLabelKey].forEachIndexed { index, label ->
                        add(
                            LegendItem(
                                icon = shapeComponent(fill(generateColor(index, numBars)), CorneredShape.Pill),
                                labelComponent = legendLabelComponent,
                                label = label,
                            )
                        )
                    }
                },
                iconSize = 10.dp,
                padding = insets(horizontal = 12.dp, vertical = 16.dp),
            ),
        ),
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End),
    )
}

private fun getColumnProvider(columns: List<LineComponent>, values: Collection<List<Float>>) =
    object : ColumnCartesianLayer.ColumnProvider {
        override fun getColumn(
            entry: ColumnCartesianLayerModel.Entry,
            seriesIndex: Int,
            extraStore: ExtraStore,
        ) = columns[seriesIndex]

        override fun getWidestSeriesColumn(seriesIndex: Int, extraStore: ExtraStore) = run {
            val longestListIndex = values.withIndex().maxByOrNull { it.value.size }?.index ?: 0
            columns[longestListIndex]
        }
    }