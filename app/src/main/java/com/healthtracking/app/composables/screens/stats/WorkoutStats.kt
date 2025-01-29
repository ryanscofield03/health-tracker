package com.healthtracking.app.composables.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.healthtracking.app.composables.graphs.workout.ExerciseProgressGraph
import com.healthtracking.app.composables.graphs.workout.WorkoutAttendanceGraph
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.WorkoutHistory
import java.time.LocalDate

@Composable
fun WorkoutStats(
    workoutData: List<WorkoutHistory>,
    workoutAttendance: Double,
    selectedExercise: String,
    exercises: List<Exercise>,
    updateSelectedExercise: (String) -> Unit,
    selectedExerciseWeightData: Map<LocalDate, List<Double>>,
    selectedExerciseRepsData: Map<LocalDate, List<Double>>
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

        Box(modifier = Modifier.weight(0.65f)) {
            // graph of increase in weight/sets
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