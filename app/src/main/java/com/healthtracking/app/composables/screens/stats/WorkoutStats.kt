package com.healthtracking.app.composables.screens.stats

import androidx.compose.runtime.Composable
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
    selectedExerciseWeightData: Map<LocalDate, List<Double>>,
    selectedExerciseRepsData: Map<LocalDate, List<Double>>
) {
    // graph of workout attendance
    WorkoutAttendanceGraph(
        workoutData = workoutData,
        workoutAttendance = workoutAttendance
    )

    // graph of increase in weight/sets
    ExerciseProgressGraph(
        selectedExercise = selectedExercise,
        exercises = exercises,
        selectedExerciseWeightData = selectedExerciseWeightData,
        selectedExerciseRepsData = selectedExerciseRepsData
    )
}