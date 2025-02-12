package com.healthtracking.app.composables.graphs.generic

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
internal fun DatedLineGraph(
    modifier: Modifier,
    stepSize: Double = 1.0,
    data: Map<LocalDate, Double>,
    startAxisTitle: String,
) {
    val hoursSlept = data.values.toList()
    val dates = data.keys.toList()

    val modelProducer = remember { CartesianChartModelProducer() }
    val bottomAxisLabelKey = ExtraStore.Key<List<String>>()
    val bottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
        context.model.extraStore[bottomAxisLabelKey][x.toInt()]
    }

    LaunchedEffect(data) {
        modelProducer.runTransaction {
            lineSeries {
                series(hoursSlept)
                extras { it[bottomAxisLabelKey] = dates.map { localDate -> localDate.format(
                    DateTimeFormatter.ofPattern("d MMM yy")) }
                }
            }
        }
    }

    CartesianChartHost(
        modifier = modifier,
        chart =
        rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(
                title = startAxisTitle,
                titleComponent = TextComponent(
                    color = MaterialTheme.colorScheme.onSurface.toArgb(),
                    textSizeSp = 15f
                ),
                itemPlacer = VerticalAxis.ItemPlacer.step(step = { stepSize })
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