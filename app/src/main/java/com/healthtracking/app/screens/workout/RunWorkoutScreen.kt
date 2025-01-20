package com.healthtracking.app.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.screens.SaveAndCancelButtons
import com.healthtracking.app.viewmodels.screen.RunWorkoutViewModel
import java.time.Duration
import java.util.Locale
import kotlin.math.max

@Composable
fun RunWorkout(
    modifier: Modifier,
    navController: NavController,
    viewModel: RunWorkoutViewModel
) {
    val openEndWorkoutConfirmationDialog = rememberSaveable { mutableStateOf(false) }
    if (openEndWorkoutConfirmationDialog.value) {
        EndWorkoutConfirmationDialog(
            onSaveWorkout =
            {
                navController.navigate("Workout")
                viewModel.saveWorkoutHistory()
                openEndWorkoutConfirmationDialog.value = false
            },
            onDismiss = { openEndWorkoutConfirmationDialog.value = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp, 16.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = viewModel.workoutName,
                style = MaterialTheme.typography.displaySmall
            )

            StopWatch(duration = viewModel.timer.value)
        }

        RunExerciseBlock(viewModel = viewModel)

        SaveAndCancelButtons(
            saveButtonLabelId = R.string.finish,
            onSave = {
                if (viewModel.validateEntries()) {
                    navController.navigate("Workout")
                    viewModel.saveWorkoutHistory()
                } else {
                    openEndWorkoutConfirmationDialog.value = true
                }

            },
            onCancel = {
                viewModel.clearViewModel()
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
            .padding(horizontal = 8.dp)
    ) {
        val openEntryDialog = rememberSaveable { mutableStateOf(false) }
        if (openEntryDialog.value) {
            EntryDialog(
                saveEntry = { viewModel.saveEntry() },
                clearEntry = { viewModel.clearEntry() },
                onDismiss = { openEntryDialog.value = false },
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
                modifier = Modifier.size(32.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(id = R.string.arrow_left),
                tint =
                    if (viewModel.canGoPreviousExercise()) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
            )

            // Exercise Name
            Text(
                text = viewModel.currentExercise.name,
                style = MaterialTheme.typography.titleMedium,
            )

            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.arrow_right),
                tint =
                    if (viewModel.canGoNextExercise()) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
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

        // Header Row
        Row(Modifier.background(MaterialTheme.colorScheme.tertiary)) {
            TableCell(text = "Weight") // TODO fix
            TableCell(text = "Reps") // TODO fix
            TableCell(text = "Weight") // TODO fix
            TableCell(text = "Reps") // TODO fix
        }
        LazyColumn(modifier = Modifier.fillMaxHeight(0.7f)) {
            // Data rows
            for (i in 0..< max(
                a = viewModel.currentExerciseEntries.size + 1,
                b = viewModel.currentExerciseHistory?.data?.size ?: 0,
            )) {
                item {
                    val entry: Pair<Int, Int>? = viewModel.currentExerciseEntries.getOrNull(i)
                    val weight = entry?.first ?: "-"
                    val reps = entry?.second ?: "-"

                    var historyWeight = "-"
                    var historyReps = "-"
                    if (viewModel.currentExerciseHistory != null) {
                        val historyData = viewModel.currentExerciseHistory!!.data.getOrNull(i)
                        historyWeight = historyData?.first?.toString() ?: "-"
                        historyReps = historyData?.second?.toString() ?: "-"
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(
                                if (i % 2 == 0) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                                else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        if (viewModel.canEditEntry(index = i)) {
                                            openEntryDialog.value = true
                                            viewModel.updateEditingEntryIndex(index = i)
                                        }
                                    }
                                )
                            }
                    ) {
                        TableCell(text = historyWeight)
                        TableCell(text = historyReps)
                        TableCell(text = weight.toString())
                        TableCell(text = reps.toString())
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = { openEntryDialog.value = true }
            ) {
                Text(text = stringResource(id = R.string.add_exercise_entry))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryDialog(
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
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    TextButton(onClick = {
                        clearEntry()
                        onDismiss()
                    }) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndWorkoutConfirmationDialog(
    onSaveWorkout: () -> Unit,
    onDismiss: () -> Unit
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
                    text = stringResource(id = R.string.finish_workout_dialog_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.finish_workout_dialog_subtitle),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onSaveWorkout() }) {
                        Text(
                            text = stringResource(id = R.string.finish_anyway),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.errorContainer
                        )
                    }
                    TextButton(onClick = { onDismiss() }) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground
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

@Composable
fun StopWatch(
    duration: Duration
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.error.copy(alpha = 0.2f + duration.seconds * 0.005.toFloat()))
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = String.format(
                Locale.US,
                "%02d:%02d",
                duration.toMinutes() % 60,
                duration.seconds % 60
            ),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}