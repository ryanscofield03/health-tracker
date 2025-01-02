package nz.ac.canterbury.seng303.healthtracking.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.healthtracking.R
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

        LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.6f)) {
            items (viewModel.exercises) { exercise ->
                Text(text = exercise, modifier = Modifier.padding(8.dp))
            }
        }

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
            onClick = { /* TODO */ },
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