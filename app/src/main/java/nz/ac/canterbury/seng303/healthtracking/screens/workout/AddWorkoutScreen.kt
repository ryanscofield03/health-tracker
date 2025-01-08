package nz.ac.canterbury.seng303.healthtracking.screens.workout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.healthtracking.R
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.screens.ErrorMessageComponent
import nz.ac.canterbury.seng303.healthtracking.screens.SaveAndCancelButtons
import nz.ac.canterbury.seng303.healthtracking.viewmodels.screen.AddWorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkout(
    modifier: Modifier,
    navController: NavController,
    viewModel: AddWorkoutViewModel,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = stringResource(id = R.string.build_workout),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Workout name input
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text(stringResource(id = R.string.workout_name_label)) },
            placeholder = { Text(stringResource(id = R.string.workout_name_placeholder)) },
            maxLines = 1,
            isError = viewModel.nameErrorMessageId != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                errorLabelColor = MaterialTheme.colorScheme.onBackground
            )
        )
        ErrorMessageComponent(
            hasError = viewModel.nameErrorMessageId != null,
            errorMessageId = viewModel.nameErrorMessageId
        )

        // Description name input
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.description,
            onValueChange = { viewModel.updateDescription(it) },
            label = { Text( stringResource(id = R.string.description_label)) },
            placeholder = { Text(stringResource(id = R.string.description_placeholder)) },
            maxLines = 2,
            isError = viewModel.nameErrorMessageId != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                errorLabelColor = MaterialTheme.colorScheme.onBackground
            )
        )
        ErrorMessageComponent(
            hasError = viewModel.descriptionErrorMessageId != null,
            errorMessageId = viewModel.descriptionErrorMessageId
        )

        // Add exercise button
        Button(
            onClick = { navController.navigate("AddExercise") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(stringResource(id = R.string.add_exercise))
        }
        Spacer(modifier = Modifier.height(7.dp))
        // Display exercises
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.55f)
            .border(
                width = 1.dp,
                color =
                if (viewModel.exercisesErrorMessageId != null) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            items (viewModel.exercises) { exercise ->
                ExerciseCard(exercise = exercise, removeExercise = { viewModel.removeExercise(exercise) })
            }
        }
        ErrorMessageComponent(
            hasError = viewModel.exercisesErrorMessageId != null,
            errorMessageId = viewModel.exercisesErrorMessageId
        )

        // Schedule workout button
        Spacer(modifier = Modifier.height(6.dp))
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
fun ExerciseCard(exercise: Exercise, removeExercise: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(60.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
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
                color = MaterialTheme.colorScheme.onSecondary
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