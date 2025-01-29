package com.healthtracking.app.composables.graphs.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.SelectionDropDown
import com.healthtracking.app.composables.graphs.generic.MultiBarDatedBarChart
import com.healthtracking.app.entities.Exercise
import java.time.LocalDate

@Composable
fun ExerciseProgressGraph(
    selectedExercise: String,
    updateSelectedExercise: (String) -> Unit,
    exercises: List<Exercise>,
    selectedExerciseWeightData: Map<LocalDate, List<Double>>,
    selectedExerciseRepsData: Map<LocalDate, List<Double>>
) {
    val exerciseMeasurementItems = listOf(
        stringResource(id = R.string.weight_measurement_label),
        stringResource(id = R.string.reps_measurement_label)
    )
    val selectedExerciseMeasurement = rememberSaveable() { mutableStateOf(exerciseMeasurementItems.first()) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // exercises dropdown
            Box(modifier = Modifier.weight(0.5f)) {
                SelectionDropDown(
                    items = exercises.map { it.name },
                    selectedText = selectedExercise,
                    onItemClick = updateSelectedExercise
                )
            }

            // reps/weight dropdown
            Box(modifier = Modifier.weight(0.5f)) {
                SelectionDropDown(
                    items = exerciseMeasurementItems,
                    selectedText = selectedExerciseMeasurement.value,
                    onItemClick = { it: String -> selectedExerciseMeasurement.value = it }
                )
            }
        }

        when (selectedExerciseMeasurement.value) {
            stringResource(id = R.string.weight_measurement_label) -> {
                MultiBarDatedBarChart(
                    modifier = Modifier.fillMaxHeight(),
                    data = selectedExerciseWeightData,
                    stepSizeY = 20.0,
                    startAxisTitle = stringResource(id = R.string.weight_graph_label)
                )
            }
            stringResource(id = R.string.reps_measurement_label) -> {
                MultiBarDatedBarChart(
                    modifier = Modifier.fillMaxHeight(),
                    data = selectedExerciseRepsData,
                    stepSizeY = 2.0,
                    startAxisTitle = stringResource(id = R.string.reps_graph_label)
                )
            }
        }
    }
}