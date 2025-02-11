package com.healthtracking.app.composables.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.healthtracking.app.R
import com.healthtracking.app.viewmodels.screen.StatsScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun StatsMain (
    modifier: Modifier,
    viewModel: StatsScreenViewModel
) {
    Column(modifier = modifier) {
        val pagerState = rememberPagerState(pageCount = { 3 })

        HorizontalPager(
            state = pagerState,
            pageSpacing = 24.dp
        ) { page ->
            val title = when (page) {
                0 -> stringResource(id = R.string.workout_screen)
                1 -> stringResource(id = R.string.eat_screen)
                2 -> stringResource(id = R.string.sleep_screen)
                else -> stringResource(id = R.string.error_screen)
            }

            // use passed in modifier here because we want to be able to see a gap between each page
            Column(modifier = Modifier
                .fillMaxHeight(0.93f)
                .fillMaxWidth()
            ) {
                // title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(Modifier.height(10.dp))

                Box {
                    // graphs
                    when (page) {
                        0 -> WorkoutStats(
                            workoutData = viewModel.getWorkoutHistory().collectAsStateWithLifecycle()
                                .value ?: listOf(),
                            workoutAttendance = viewModel.getWorkoutAttendance(),
                            selectedExercise = viewModel.selectedExercise.collectAsStateWithLifecycle()
                                .value ?: stringResource(id = R.string.select_exercise),
                            exercises = viewModel.getExercises().collectAsState().value ?: listOf(),
                            updateSelectedExercise = { viewModel.updateSelectedExercise(it) },
                            selectedExerciseWeightData = viewModel.getSelectedExerciseWeightData(),
                            selectedExerciseRepsData = viewModel.getSelectedExerciseRepData()
                        )

                        1 -> EatStats(
                            caloriesData = viewModel.getCaloriesData()
                                .collectAsStateWithLifecycle(mapOf()).value,
                            proteinData = viewModel.getProteinData()
                                .collectAsStateWithLifecycle(mapOf()).value,
                            carbsData = viewModel.getCarbsData()
                                .collectAsStateWithLifecycle(mapOf()).value,
                            fatsData = viewModel.getFatsData()
                                .collectAsStateWithLifecycle(mapOf()).value,
                            caloriesGoal = viewModel.caloriesGoal
                                .collectAsStateWithLifecycle().value.toDouble(),
                            proteinGoal = viewModel.proteinGoal
                                .collectAsStateWithLifecycle().value.toDouble(),
                            carbsGoal = viewModel.carbsGoal
                                .collectAsStateWithLifecycle().value.toDouble(),
                            fatsGoal = viewModel.fatsGoal
                                .collectAsStateWithLifecycle().value.toDouble()
                        )

                        2 -> SleepStats(
                            sleepHoursData = viewModel.getSleepHoursData(),
                            sleepRatingsData = viewModel.getSleepRatingsData()
                        )

                        else -> Text(text = stringResource(id = R.string.error_string))
                    }
                }
            }
        }
        PageSelectedIcons(pagerState = pagerState)
    }
}

@Composable
private fun PageSelectedIcons(
    pagerState: PagerState,
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    if (dragAmount.x > 0) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    } else if (dragAmount.x < 0) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(pagerState.pageCount) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(8.dp)
                    .clip(shape = CircleShape)
                    .background(
                        color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                    .clickable {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
            )
        }
    }
}
