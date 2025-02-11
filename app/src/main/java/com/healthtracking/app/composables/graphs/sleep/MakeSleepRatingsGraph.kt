package com.healthtracking.app.composables.graphs.sleep

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
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