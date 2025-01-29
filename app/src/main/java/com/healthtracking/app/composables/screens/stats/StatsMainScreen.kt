package com.healthtracking.app.composables.screens.stats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.ScreenHeader
import com.healthtracking.app.viewmodels.screen.StatsScreenViewModel

@Composable
fun StatsMain (
    modifier: Modifier,
    viewModel: StatsScreenViewModel
) {
    Column(modifier = modifier) {
        ScreenHeader(
            headerStringId = R.string.stats_screen,
            spacerSize = 8.dp
        )

        val pagerState = rememberPagerState(pageCount = {
            3
        })

        HorizontalPager(state = pagerState) { page ->
            val title = when (page) {
                0 -> stringResource(id = R.string.workout_screen)
                1 -> stringResource(id = R.string.eat_screen)
                2 -> stringResource(id = R.string.sleep_screen)
                else -> stringResource(id = R.string.error_screen)
            }

            Column(modifier = Modifier.fillMaxSize()) {
                // title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(Modifier.height(10.dp))

                Box(modifier = Modifier.padding(8.dp)) {
                    // graphs
                    when (page) {
                        0 -> WorkoutStats(
                            workoutData = viewModel.getWorkoutHistory(),
                            workoutAttendance = viewModel.getWorkoutAttendance(),
                            selectedExercise = viewModel.getSelectedExercise(),
                            exercises = viewModel.getExercises(),
                            updateSelectedExercise = { viewModel.updateSelectedExercise(it) },
                            selectedExerciseWeightData = viewModel.getSelectedExerciseWeightData(),
                            selectedExerciseRepsData = viewModel.getSelectedExerciseRepsData()
                        )
                        1 -> EatStats(
                            caloriesData = viewModel.getCaloriesData(),
                            proteinData = viewModel.getProteinData(),
                            carbsData = viewModel.getCarbsData(),
                            fatsData = viewModel.getFatsData()
                        )
                        2 -> SleepStats(
                            hoursSleptData = viewModel.getHoursSleptData(),
                            sleepRatingsData = viewModel.getSleepRatingsData()
                        )
                        else -> Text(text = stringResource(id = R.string.error_string))
                    }
                }
            }
        }
    }
}