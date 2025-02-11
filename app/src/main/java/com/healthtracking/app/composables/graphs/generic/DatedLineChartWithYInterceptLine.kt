package com.healthtracking.app.composables.graphs.generic

import androidx.compose.foundation.layout.height
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
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.Position
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.floor

val bottomAxisLabelKey = ExtraStore.Key<List<String>>()

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
    val hoursSlept = data.values.toList().ifEmpty { listOf(0.0) }
    val dates = data.keys.toList().ifEmpty { listOf(LocalDate.now()) }

    val modelProducer = remember { CartesianChartModelProducer() }
    val bottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
        context.model.extraStore[bottomAxisLabelKey][x.toInt()]
    }

    val areaFill: LineCartesianLayer.AreaFill? =
        if (hasGradient) {
            LineCartesianLayer.AreaFill.single(
                fill(
                    ShaderProvider.verticalGradient(
                        arrayOf(lineColor.copy(alpha = 0.2f), lineColor.copy(alpha = 0f))
                    )
                )
            )
        } else {
            null
        }

    val rangeProvider = object : CartesianLayerRangeProvider
    {
        override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
            stepSize * floor((minY / stepSize)) // round down to minimum step

        override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
            stepSize * ceil((maxOf(maxY, interceptY)) / stepSize) // round up to the next step
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
                ),
                rangeProvider = rangeProvider
            ),
            startAxis = VerticalAxis.rememberStart(
                size = BaseAxis.Size.Auto(),
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
                getHorizontalLine(
                    label = interceptLineLabel,
                    height = interceptY
                )
            )
        ),
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End),
    )
}

/**
 * Mostly copy and pasted from sample
 */
@Composable
private fun getHorizontalLine(label: String, height: Double): HorizontalLine {
    val fill = Fill(MaterialTheme.colorScheme.tertiary.toArgb())
    return HorizontalLine(
        y = { height },
        line = LineComponent(fill = fill, thicknessDp = 2f),
        labelComponent =
        TextComponent(
            margins = Insets(startDp = 6f),
            padding = Insets(startDp = 8f, endDp = 8f, bottomDp = 2f),
            background = ShapeComponent(
                fill = fill,
                shape = CorneredShape.rounded(bottomLeftDp = 4f, bottomRightDp = 4f)
            ),
        ),
        label = { label },
        horizontalLabelPosition = Position.Horizontal.End,
        verticalLabelPosition = Position.Vertical.Center,
    )
}