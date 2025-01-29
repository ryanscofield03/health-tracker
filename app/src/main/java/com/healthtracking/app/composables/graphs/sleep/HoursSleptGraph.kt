package com.healthtracking.app.composables.graphs.sleep

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.healthtracking.app.R
import com.healthtracking.app.composables.graphs.generic.DatedBarChart
import java.time.LocalDate

@Composable
fun MakeHoursSleptGraph(
    modifier: Modifier,
    data: Map<LocalDate, Double>
) {
    DatedBarChart(
        modifier = modifier,
        stepSizeY = 2.0,
        data = data,
        startAxisTitle = stringResource(id = R.string.hours_slept_axis_title),
    )
}
