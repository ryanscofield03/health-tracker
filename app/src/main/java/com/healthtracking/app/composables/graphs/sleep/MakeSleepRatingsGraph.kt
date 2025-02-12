package com.healthtracking.app.composables.graphs.sleep

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.healthtracking.app.composables.graphs.generic.DatedBarChart
import java.time.LocalDate

@Composable
fun MakeSleepRatingsGraph(
    modifier: Modifier,
    data: Map<LocalDate, Float>
) {
    DatedBarChart(
        modifier = modifier,
        data = data,
        maxYValue = 5.0
    )
}