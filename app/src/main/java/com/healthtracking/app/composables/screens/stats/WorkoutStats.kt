package com.healthtracking.app.composables.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.BackgroundBorderBox
import com.healthtracking.app.composables.GraphWithTitle
import com.healthtracking.app.composables.graphs.workout.ExerciseProgressGraph
import com.healthtracking.app.composables.graphs.workout.WorkoutAttendanceGraph
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.WorkoutHistory
import java.time.LocalDate

@Composable
fun WorkoutStats(
    workoutData: List<WorkoutHistory>,
    workoutAttendance: Float,
    selectedExercise: String,
    exercises: List<Exercise>,
    updateSelectedExercise: (String) -> Unit,
    selectedExerciseWeightData: Map<LocalDate, List<Float>>,
    selectedExerciseRepsData: Map<LocalDate, List<Float>>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(modifier = Modifier.weight(0.35f)) {
            // graph of workout attendance
            WorkoutAttendanceGraph(
                workoutData = workoutData,
                workoutAttendance = workoutAttendance
            )
        }

        Box(modifier = Modifier.weight(0.6f)) {
            GraphWithTitle(
                title = stringResource(id = R.string.exercise_progress_title),
                modifier = Modifier.fillMaxSize()
            ) {
                ExerciseProgressGraph(
                    selectedExercise = selectedExercise,
                    exercises = exercises,
                    updateSelectedExercise = updateSelectedExercise,
                    selectedExerciseWeightData = selectedExerciseWeightData,
                    selectedExerciseRepsData = selectedExerciseRepsData
                )
            }
        }
    }
}