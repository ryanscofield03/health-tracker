package com.healthtracking.app.composables.graphs.workout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.healthtracking.app.composables.graphs.generic.MultiBarDatedBarChart
import com.healthtracking.app.entities.Exercise
import java.time.LocalDate

@Composable
fun ExerciseProgressGraph(
    selectedExercise: String,
    exercises: List<Exercise>,
    selectedExerciseWeightData: Map<LocalDate, List<Double>>,
    selectedExerciseRepsData: Map<LocalDate, List<Double>>
) {
    val selectedExerciseMeasurement = rememberSaveable() { mutableStateOf("weight") }

    // exercises dropdown

    // reps/weight dropdown

    when (selectedExerciseMeasurement.value) {
        "weight" -> MultiBarDatedBarChart(data = selectedExerciseWeightData)
        "reps" -> MultiBarDatedBarChart(data = selectedExerciseRepsData)
    }
}