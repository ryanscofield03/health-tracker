package com.healthtracking.app.composables.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.composables.SaveAndCancelButtons
import com.healthtracking.app.composables.table.TableCell
import com.healthtracking.app.composables.table.TableCellType
import com.healthtracking.app.services.toStringWithDecimalPoints
import com.healthtracking.app.theme.CustomCutCornerShape
import com.healthtracking.app.viewmodels.screen.RunWorkoutViewModel
import java.time.Duration
import java.util.Locale

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
        modifier = modifier.fillMaxSize(),
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

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(8.dp))

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
private fun RunExerciseBlock(viewModel: RunWorkoutViewModel) {
    Box(modifier = Modifier.fillMaxHeight(0.88f)) {
        val numberOfPages = viewModel.exerciseHistory.collectAsStateWithLifecycle().value.size
        when {
            numberOfPages > 0 -> {
                val pagerState = rememberPagerState { numberOfPages }

                val openEntryDialog = rememberSaveable { mutableStateOf(false) }
                if (openEntryDialog.value) {
                    ExerciseEntryDialog(
                        saveEntry = { viewModel.saveEntry(pagerState.currentPage) },
                        clearEntry = { viewModel.clearEntry() },
                        onDismissRequest = { openEntryDialog.value = false },
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        pageSpacing = 20.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) { pageIndex ->
                        Column {
                            ExerciseBlockHeader(
                                exerciseName = viewModel.getCurrentExercise(pageIndex).name,
                                canGoPreviousExercise = viewModel.canGoPreviousExercise(pageIndex),
                                canGoNextExercise = viewModel.canGoNextExercise(pageIndex)
                            )
                            ExerciseTableHeader(
                                exerciseHistoryDate =
                                viewModel.getCurrentExerciseHistoryDate(pageIndex) ?: stringResource(R.string.no_date)
                            )
                            ExerciseTableData(
                                numRows = viewModel.getNumberOfRows(pageIndex),
                                exerciseEntries = viewModel.getExerciseEntries(pageIndex),
                                historyData = viewModel.getHistoryEntries(pageIndex),
                                editExerciseEntry = {
                                    if (viewModel.canEditEntry(exerciseIndex = pageIndex, entryIndex = it)) {
                                        openEntryDialog.value = true
                                        viewModel.updateEditingEntryIndex(exerciseIndex = pageIndex, entryIndex = it)
                                    }
                                }
                            )
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
            else -> {}
        }
    }
}

@Composable
private fun ExerciseTableData(
    numRows: Int,
    exerciseEntries: List<Pair<Float, Int>>,
    historyData: List<Pair<Float, Int>?>,
    editExerciseEntry: (Int) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxHeight(0.7f)) {
        // Data rows
        for (i in 0..<numRows) {
            item {
                val entry: Pair<Float, Int>? = exerciseEntries.getOrNull(i)
                val weight: String = entry?.first?.toStringWithDecimalPoints() ?: "-"
                val reps: String = entry?.second?.toString() ?: "-"
                val historyEntry: Pair<Float, Int>? = historyData.getOrNull(i)
                val historyWeight = historyEntry?.first?.toStringWithDecimalPoints() ?: "-"
                val historyReps = historyEntry?.second?.toString() ?: "-"

                Row(
                    Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = { editExerciseEntry(i) }
                            )
                        }
                ) {
                    val backgroundColor = if (i % 2 == 0) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                    else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)

                    TableCell(
                        text = historyWeight,
                        backgroundColor = backgroundColor
                    )
                    TableCell(
                        text = historyReps,
                        backgroundColor = backgroundColor
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    TableCell(
                        text = weight,
                        backgroundColor = backgroundColor
                    )
                    TableCell(
                        text = reps,
                        backgroundColor = backgroundColor
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseTableHeader(
    exerciseHistoryDate: String = "",
) {
    Spacer(modifier = Modifier.height(8.dp))
    // Header Row
    Row(modifier = Modifier.height(70.dp)) {
        Column(modifier = Modifier.weight(0.5f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    text = exerciseHistoryDate,
                )
            }
            Row(modifier = Modifier.weight(0.5f)) {
                TableCell(
                    type = TableCellType.TOP_LEFT,
                    text = "Weight",
                    textColor = MaterialTheme.colorScheme.onTertiary,
                    backgroundColor = MaterialTheme.colorScheme.tertiary
                )
                TableCell(
                    type = TableCellType.TOP_RIGHT,
                    text = "Reps",
                    textColor = MaterialTheme.colorScheme.onTertiary,
                    backgroundColor = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(0.5f)
                .align(Alignment.Bottom)
        ) {
            Row {
                TableCell(
                    type = TableCellType.TOP_LEFT,
                    text = "Weight",
                    textColor = MaterialTheme.colorScheme.onTertiary,
                    backgroundColor = MaterialTheme.colorScheme.tertiary
                )
                TableCell(
                    type = TableCellType.TOP_RIGHT,
                    text = "Reps",
                    textColor = MaterialTheme.colorScheme.onTertiary,
                    backgroundColor = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
private fun ExerciseBlockHeader(
    exerciseName: String,
    canGoPreviousExercise: Boolean,
    canGoNextExercise: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = stringResource(id = R.string.arrow_left),
            tint =
            if (canGoPreviousExercise) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
        )

        // Exercise Name
        Text(
            text = exerciseName,
            style = MaterialTheme.typography.titleMedium,
        )

        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(id = R.string.arrow_right),
            tint =
            if (canGoNextExercise) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EndWorkoutConfirmationDialog(
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
private fun StopWatch(
    duration: Duration
) {
    val errorAlphaValue = 0.2f + duration.seconds * 0.005.toFloat()

    Box(
        modifier = Modifier
            .clip(CustomCutCornerShape)
            .background(color = MaterialTheme.colorScheme.error.copy(alpha = errorAlphaValue))
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