package com.healthtracking.app.composables.graphs.sleep

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.healthtracking.app.R
import com.healthtracking.app.composables.graphs.generic.DatedBarChart
import java.time.LocalDate

@Composable
fun MakeSleepRatingsGraph(
    modifier: Modifier,
    data: Map<LocalDate, Double>
) {
    DatedBarChart(
        modifier = modifier,
        data = data,
        startAxisTitle = stringResource(id = R.string.sleep_ratings_axis_title),
    )
}