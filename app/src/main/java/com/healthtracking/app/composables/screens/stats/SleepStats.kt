package com.healthtracking.app.composables.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.healthtracking.app.composables.graphs.sleep.MakeHoursSleptGraph
import com.healthtracking.app.composables.graphs.sleep.MakeSleepRatingsGraph
import java.time.LocalDate

@Composable
fun SleepStats(
    hoursSleptData: Map<LocalDate, Double>,
    sleepRatingsData: Map<LocalDate, Double>
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // graph of hours slept
        MakeHoursSleptGraph(
            modifier = Modifier,
            data = hoursSleptData
        )

        // graph of ratings
        MakeSleepRatingsGraph(
            modifier = Modifier,
            data = sleepRatingsData
        )
    }
}