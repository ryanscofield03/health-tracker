package com.healthtracking.app.composables.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.healthtracking.app.composables.BackgroundBorderBox
import com.healthtracking.app.composables.graphs.sleep.MakeHoursSleptGraph
import com.healthtracking.app.composables.graphs.sleep.MakeSleepRatingsGraph
import com.healthtracking.app.entities.Sleep
import java.time.LocalDate

@Composable
fun SleepStats(
    sleepHoursData: Map<LocalDate, Float>,
    sleepRatingsData: Map<LocalDate, Float>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        BackgroundBorderBox {
            // graph of hours slept
            MakeHoursSleptGraph(
                modifier = Modifier,
                data = sleepHoursData
            )
        }

        BackgroundBorderBox {
            // graph of ratings
            MakeSleepRatingsGraph(
                modifier = Modifier,
                data = sleepRatingsData
            )
        }
    }
}