package com.healthtracking.app.composables.graphs.sleep

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.healthtracking.app.composables.graphs.generic.DatedBarChart
import java.time.LocalDate

@Composable
fun MakeHoursSleptGraph(
    modifier: Modifier,
    data: Map<LocalDate, Float>
) {
    DatedBarChart(
        modifier = modifier,
        stepSizeY = 2f,
        data = data,
    )
}
