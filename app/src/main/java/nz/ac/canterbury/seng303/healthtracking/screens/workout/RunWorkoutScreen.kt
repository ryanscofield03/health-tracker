package nz.ac.canterbury.seng303.healthtracking.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.healthtracking.R
import nz.ac.canterbury.seng303.healthtracking.screens.SaveAndCancelButtons
import nz.ac.canterbury.seng303.healthtracking.viewmodels.screen.RunWorkoutViewModel

@Composable
fun RunWorkout(
    modifier: Modifier,
    navController: NavController,
    viewModel: RunWorkoutViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = viewModel.workoutName,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.align(Alignment.Start)
        )

        RunExerciseBlock(viewModel = viewModel)

        SaveAndCancelButtons(
            saveButtonLabelId = R.string.complete,
            onSave = {
                navController.navigate("Workout")
                viewModel.saveWorkoutHistory()
            },
            onCancel = {
                /* TODO clear view model? */
                navController.navigate("Workout")
            }
        )
    }
}

@Composable
fun RunExerciseBlock(viewModel: RunWorkoutViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.88f)
            .padding(8.dp)
    ) {
        val openEntryPopup = rememberSaveable { mutableStateOf(false) }
        if (openEntryPopup.value) {
            EntryPopup(
                saveEntry = { viewModel.saveEntry() },
                clearEntry = { viewModel.clearEntry() },
                onDismiss = { openEntryPopup.value = false },
                weight = viewModel.newWeight,
                updateWeight = { viewModel.updateNewWeight(it) },
                validWeight = viewModel.newWeightIsValid(),
                weightErrorMessageId = R.string.weight_error_message,
                reps = viewModel.newReps,
                updateReps = { viewModel.updateNewReps(it) },
                validReps = viewModel.newRepsIsValid(),
                repsErrorMessageId = R.string.reps_error_message,
            )
        }

        val timeoutMillis = 200L
        val lastActionTime = rememberSaveable{ mutableLongStateOf(0L) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            val currentTime = System.currentTimeMillis()
                            if (currentTime.minus(lastActionTime.longValue) >= timeoutMillis) {
                                lastActionTime.longValue = currentTime
                                when {
                                    // Dragging left
                                    dragAmount < 0 -> {
                                        viewModel.goToNextExercise()
                                    }
                                    // Dragging right
                                    dragAmount > 0 -> {
                                        viewModel.goToPreviousExercise()
                                    }
                                }
                            }
                        }
                    )
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(id = R.string.arrow_left),
                tint =
                    if (viewModel.canGoPreviousExercise()) MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            )

            // Exercise Name
            Text(
                text = viewModel.currentExercise.name,
                style = MaterialTheme.typography.titleMedium,
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.arrow_right),
                tint =
                    if (viewModel.canGoNextExercise()) MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = "Previous"
            )
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = "Current"
            )
        }
        LazyColumn {
            // Header Row
            item {
                Row(Modifier.background(MaterialTheme.colorScheme.tertiary)) {
                    TableCell(text = "Weight") // TODO fix
                    TableCell(text = "Reps") // TODO fix
                    TableCell(text = "Weight") // TODO fix
                    TableCell(text = "Reps") // TODO fix
                }
            }
            itemsIndexed(viewModel.currentExerciseEntries) { index, entry ->
                val (weight, sets) = entry

                if (viewModel.currentExerciseHistory != null) {
                    val historyData = viewModel.currentExerciseHistory!!.data.getOrNull(index)
                    val historyWeight = historyData?.first?.toString() ?: "-"
                    val historyReps = historyData?.second?.toString() ?: "-"

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(
                                if (index % 2 == 0) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                                else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
                            )
                    ) {
                        TableCell(text = historyWeight)
                        TableCell(text = historyReps)
                        TableCell(text = weight.toString())
                        TableCell(text = sets.toString())
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = { openEntryPopup.value = true }
            ) {
                Text(text = stringResource(id = R.string.add_exercise_entry))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPopup(
    saveEntry: () -> Boolean,
    clearEntry: () -> Unit,
    onDismiss: () -> Unit,
    weight: String, // TODO make this and other lines dynamic later
    updateWeight: (String) -> Unit,
    validWeight: Boolean,
    weightErrorMessageId: Int,
    reps: String,
    updateReps: (String) -> Unit,
    validReps: Boolean,
    repsErrorMessageId: Int
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.exercise_entry_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Weight Input Field
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = weight,
                    onValueChange = updateWeight,
                    label = { Text(stringResource(id = R.string.weight_label)) },
                    placeholder = { Text(stringResource(id = R.string.weight_placeholder)) },
                    maxLines = 1,
                    isError = !validWeight,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        errorBorderColor = MaterialTheme.colorScheme.error
                    )
                )
                if (!validWeight) {
                    Text(
                        text = stringResource(id = weightErrorMessageId),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                // Reps Input Field
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = reps,
                    onValueChange = updateReps,
                    label = { Text(stringResource(id = R.string.reps_label)) },
                    placeholder = { Text(stringResource(id = R.string.reps_placeholder)) },
                    maxLines = 1,
                    isError = !validReps,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        errorBorderColor = MaterialTheme.colorScheme.error
                    )
                )
                if (!validReps) {
                    Text(
                        text = stringResource(id = repsErrorMessageId),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick =
                    {
                        if (saveEntry()) {onDismiss()}
                    }) {
                        Text(
                            text = stringResource(id = R.string.add),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    TextButton(onClick = {
                        clearEntry()
                        onDismiss()
                    }) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(4.dp)
            .weight(0.25f)
            .height(40.dp)
            .fillMaxSize()
    ) {
        Text(text = text)
    }
}