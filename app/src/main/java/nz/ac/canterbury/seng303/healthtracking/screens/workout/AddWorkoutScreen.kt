package nz.ac.canterbury.seng303.healthtracking.screens.workout

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.healthtracking.R
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.viewmodels.screen.AddWorkoutViewModel
import org.koin.androidx.compose.getViewModel

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
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.build_workout),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        OutlinedTextField(
            value = viewModel.name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text(stringResource(id = R.string.workout_name)) },
            placeholder = { Text("Enter workout name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.description,
            onValueChange = { viewModel.updateDescription(it) },
            label = { Text("Description") },
            placeholder = { Text("Enter a short description") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )

        Button(
            onClick = { navController.navigate("AddExercise") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(stringResource(id = R.string.add_exercise))
        }

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)) {
            items (viewModel.exercises) { exercise ->
                ExerciseCard(exercise)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { navController.navigate("ScheduleWorkout") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(stringResource(id = R.string.schedule))
        }

        Button(
            onClick = {
                navController.navigate("Workout")
                viewModel.save()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(stringResource(id = R.string.save))
        }
    }
}

@Composable
fun ExerciseCard(exercise: Exercise) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
        ),
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(text = exercise.name)
        }
    }
}