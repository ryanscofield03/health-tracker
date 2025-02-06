package com.healthtracking.app.composables.screens.workout

import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.composables.ErrorMessageComponent
import com.healthtracking.app.composables.HeaderAndListBox
import com.healthtracking.app.composables.SaveAndCancelButtons
import com.healthtracking.app.composables.TextFieldWithErrorMessage
import com.healthtracking.app.composables.screens.eat.AddMealEntry
import com.healthtracking.app.viewmodels.screen.AddWorkoutViewModel

@Composable
fun BuildWorkout(
    modifier: Modifier,
    navController: NavController,
    viewModel: AddWorkoutViewModel,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Workout name input
        TextFieldWithErrorMessage(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.name,
            onValueChange = { viewModel.updateName(it) },
            labelId = R.string.workout_name_label,
            placeholderId = R.string.workout_name_placeholder,
            hasError = viewModel.nameErrorMessageId != null,
            errorMessageId = viewModel.nameErrorMessageId
        )

        TextFieldWithErrorMessage(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.description,
            onValueChange = { viewModel.updateDescription(it) },
            labelId = R.string.description_label,
            placeholderId = R.string.description_placeholder,
            hasError = viewModel.descriptionErrorMessageId != null,
            errorMessageId = viewModel.descriptionErrorMessageId
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
                LazyColumn() {
                    items (viewModel.exercises) { exercise ->
                        ExerciseCard(exercise = exercise, removeExercise = { viewModel.removeExercise(exercise) })
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
                if (viewModel.isValid()) {
                    navController.navigate("Workout")
                    viewModel.save()
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
    removeExercise: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(60.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyMedium,
            )
            IconButton(onClick = { removeExercise() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.delete),
                    contentDescription = stringResource(id = R.string.delete)
                )
            }
        }
    }
}