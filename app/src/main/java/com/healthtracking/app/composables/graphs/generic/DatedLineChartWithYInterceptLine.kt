package com.healthtracking.app.composables.graphs.generic

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Position
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
internal fun DatedLineChartWithYInterceptLine(
    modifier: Modifier = Modifier,
    stepSize: Double = 1.0,
    lineColor: Color,
    hasGradient: Boolean = true,
    interceptLineLabel: String,
    interceptY: Double = 0.0,
    data: Map<LocalDate, Double>,
    startAxisTitle: String? = null,
) {
    val hoursSlept = data.values.toList()
    val dates = data.keys.toList()

    val modelProducer = remember { CartesianChartModelProducer() }
    val bottomAxisLabelKey = ExtraStore.Key<List<String>>()
    val bottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
        context.model.extraStore[bottomAxisLabelKey][x.toInt()]
    }

    val areaFill: LineCartesianLayer.AreaFill? =
        if (hasGradient) {
            LineCartesianLayer.AreaFill.single(
                fill(
                    ShaderProvider.verticalGradient(
                        arrayOf(lineColor.copy(alpha = 0.6f), lineColor.copy(alpha = 0.2f))
                    )
                )
            )
        } else {
            null
        }

    LaunchedEffect(Unit) {
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
            rememberLineCartesianLayer(
                lineProvider =
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(fill(color = lineColor)),
                        areaFill = areaFill,
                    )
                )
            ),
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
                label = TextComponent(
                    textSizeSp = 12f,
                    color = MaterialTheme.colorScheme.onSurface.toArgb()
                ),
                valueFormatter = bottomAxisValueFormatter,
            ),
            decorations = listOf(
                rememberComposeHorizontalLine(label = interceptLineLabel, height = interceptY)
            )
        ),
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End),
    )
}

@Composable
private fun rememberComposeHorizontalLine(
    label: String,
    height: Double
): HorizontalLine {
    val fill = fill(Color(MaterialTheme.colorScheme.surfaceVariant.toArgb()))
    val line = rememberLineComponent(fill = fill, thickness = 2.dp)
    val labelComponent =
        rememberTextComponent(
            margins = insets(start = 6.dp),
            padding = insets(start = 8.dp, end = 8.dp, bottom = 2.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            background = shapeComponent(
                fill = fill,
                shape = CorneredShape.rounded(bottomLeft = 4.dp, bottomRight = 4.dp)
            )
        )
    return remember {
        HorizontalLine(
            y = { height },
            line = line,
            labelComponent = labelComponent,
            label = { label },
            verticalLabelPosition = Position.Vertical.Bottom,
        )
    }
}