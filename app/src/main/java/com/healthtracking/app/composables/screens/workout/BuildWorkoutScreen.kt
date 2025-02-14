package com.healthtracking.app.composables.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.composables.HeaderAndListBox
import com.healthtracking.app.composables.SaveAndCancelButtons
import com.healthtracking.app.composables.TextFieldWithErrorMessage
import com.healthtracking.app.theme.CustomCutCornerShape
import com.healthtracking.app.viewmodels.screen.BuildWorkoutViewModel

@Composable
fun BuildWorkout(
    modifier: Modifier,
    navController: NavController,
    viewModel: BuildWorkoutViewModel,
) {
    val editExerciseDialogOpen = rememberSaveable { mutableStateOf(false) }
    EditExerciseDialog(
        onDismissRequest = { editExerciseDialogOpen.value = false; viewModel.clearEditExerciseDialog() },
        exerciseName = viewModel.editExerciseDialogName,
        exerciseMetrics = viewModel.editExerciseDialogMetrics,
        updateMetrics = { viewModel.updateDialogMetrics(it)},
        onSave = { viewModel.saveEditExerciseDialog() },
        isOpen = editExerciseDialogOpen.value
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Workout name input
        TextFieldWithErrorMessage(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.name,
            onValueChange = { viewModel.updateName(it) },
            label = stringResource(id = R.string.workout_name_label),
            placeholder = stringResource(id = R.string.workout_name_placeholder),
            hasSaved = viewModel.alreadySaved,
            hasError = !viewModel.isNameValid(),
            errorMessage = stringResource(id = R.string.name_error_message)
        )

        TextFieldWithErrorMessage(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.description,
            onValueChange = { viewModel.updateDescription(it) },
            label = stringResource(id = R.string.description_label),
            placeholder = stringResource(id = R.string.description_placeholder),
            hasSaved = viewModel.alreadySaved,
            hasError = !viewModel.isDescriptionValid(),
            errorMessage = stringResource(id = R.string.description_error_message)
        )

        Spacer(modifier = Modifier.height(7.dp))

        HeaderAndListBox(
            modifier = Modifier.fillMaxHeight(0.7f),
            header = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.exercises_title),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                    // Allow for adding exercise
                    AddExerciseButton(onClick = { navController.navigate("AddExercise") })
                }
            },
            listContent = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items (viewModel.exercises) { exercise ->
                        ExerciseCard(
                            exercise = exercise,
                            openEditDialog = { editExerciseDialogOpen.value = true },
                            populateEditDialog = { viewModel.populateEditDialog(exercise) }
                        )
                    }
                }
            },
            isContentEmpty = viewModel.exercises.isEmpty(),
            contentPlaceholderText = stringResource(id = R.string.no_existing_exercises)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Schedule workout button
        Button(
            onClick = { navController.navigate("ScheduleWorkout") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.schedule),
                    contentDescription = stringResource(id = R.string.schedule),
                )
                Text(stringResource(id = R.string.schedule))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))

        // Save and cancel buttons
        SaveAndCancelButtons(
            onSave = {
                viewModel.save()
                if (viewModel.isValid()) {
                    navController.navigate("Workout")
                }
            },
            onCancel = {
                navController.navigate("Workout")
                viewModel.clear()
            }
        )
    }
}

@Composable
private fun AddExerciseButton(
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier.size(20.dp),
        onClick = onClick
    ) {
        Icon(imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add_exercise)
        )
    }
}

@Composable
private fun ExerciseCard(
    exercise: Exercise,
    openEditDialog: () -> Unit,
    populateEditDialog: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = CustomCutCornerShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyMedium,
            )
            IconButton(
                modifier = Modifier.size(20.dp),
                onClick = { openEditDialog(); populateEditDialog()})
            {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.edit)
                )
            }
        }
    }
}