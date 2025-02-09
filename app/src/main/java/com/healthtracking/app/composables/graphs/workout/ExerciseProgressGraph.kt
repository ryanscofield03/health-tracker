package com.healthtracking.app.composables.graphs.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
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
    selectedExerciseWeightData: Map<LocalDate, List<Float>>,
    selectedExerciseRepsData: Map<LocalDate, List<Float>>
) {
    val exerciseMeasurementItems: List<String> = listOf(
        stringResource(id = R.string.weight_measurement_label),
        stringResource(id = R.string.reps_measurement_label)
    )
    val selectedExerciseMeasurement = rememberSaveable() { mutableStateOf(exerciseMeasurementItems.first()) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // exercises dropdown
            Box(modifier = Modifier.weight(0.5f)) {
                SelectionDropDown(
                    label = stringResource(id = R.string.select_exercise),
                    items = exercises.map { it.name },
                    selectedText = selectedExercise,
                    onItemClick = updateSelectedExercise
                )
            }

            // reps/weight dropdown
            Box(modifier = Modifier.weight(0.5f)) {
                SelectionDropDown(
                    label = stringResource(id = R.string.select_measurement_stats),
                    items = exerciseMeasurementItems,
                    selectedText = selectedExerciseMeasurement.value,
                    onItemClick = { selectedExerciseMeasurement.value = it }
                )
            }
        }
        if (selectedExerciseWeightData.isNotEmpty() && selectedExerciseRepsData.isNotEmpty()) {
            when (selectedExerciseMeasurement.value) {
                stringResource(id = R.string.weight_measurement_label) -> {
                    MultiBarDatedBarChart(
                        modifier = Modifier.fillMaxHeight(),
                        data = selectedExerciseWeightData,
                        stepSizeY = 10.0,
                        startAxisTitle = stringResource(id = R.string.weight_graph_label)
                    )
                }
                stringResource(id = R.string.reps_measurement_label) -> {
                    MultiBarDatedBarChart(
                        modifier = Modifier.fillMaxHeight(),
                        data = selectedExerciseRepsData,
                        stepSizeY = 1.0,
                        startAxisTitle = stringResource(id = R.string.reps_graph_label)
                    )
                }
                else -> {
                    ErrorMessageBox(
                        errorMessage = stringResource(id = R.string.unselected_exercise_graph_error)
                    )
                }
            }
        } else {
            ErrorMessageBox(
                errorMessage = stringResource(id = R.string.missing_exercise_graph_data)
            )
        }
    }
}

@Composable
private fun ErrorMessageBox(
    errorMessage: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = errorMessage)
    }
}