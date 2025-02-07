package com.healthtracking.app.composables.graphs.sleep

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import com.healthtracking.app.R
import com.healthtracking.app.composables.graphs.generic.DatedBarChart
import com.healthtracking.app.entities.Sleep
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
        startAxisTitle = stringResource(id = R.string.hours_slept_axis_title),
    )
}
