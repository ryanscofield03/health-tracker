package com.healthtracking.app.composables.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.GraphWithTitle
import com.healthtracking.app.composables.graphs.sleep.MakeHoursSleptGraph
import com.healthtracking.app.composables.graphs.sleep.MakeSleepRatingsGraph
import java.time.LocalDate

@Composable
fun SleepStats(
    sleepHoursData: Map<LocalDate, Float>,
    sleepRatingsData: Map<LocalDate, Float>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        GraphWithTitle(
            modifier = Modifier.weight(0.5f),
            title = stringResource(id = R.string.hours_slept_title),
        ) {
            MakeHoursSleptGraph(
                modifier = Modifier,
                data = sleepHoursData
            )
        }

        GraphWithTitle(
            modifier = Modifier.weight(0.5f),
            title = stringResource(id = R.string.sleep_ratings_title),
        ) {
            MakeSleepRatingsGraph(
                modifier = Modifier,
                data = sleepRatingsData
            )
        }
    }
}