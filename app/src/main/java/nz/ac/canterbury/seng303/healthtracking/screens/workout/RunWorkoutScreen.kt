package nz.ac.canterbury.seng303.healthtracking.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.healthtracking.R
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
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
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        RunExerciseBlock(
            exercise = viewModel.currentExercise,
            exerciseEntry = viewModel.currentExerciseEntry
        )

        SaveAndCancelButtons(
            saveButtonLabelId = R.string.complete,
            onSave = {
                /* TODO save the workout information */
                navController.navigate("Workout")
            },
            onCancel = {
                /* TODO clear view model? */
                navController.navigate("Workout")
            }
        )
    }
}

@Composable
fun RunExerciseBlock(
    exercise: Exercise,
    exerciseEntry: List<Pair<Int, Int>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Exercise Name
        Text(
            text = exercise.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Exercise entry headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Weight", // TODO FIX - allow dynamic & use string resources
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Sets", // TODO FIX - allow dynamic & use string resources
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }

        exerciseEntry.forEach { dataPair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dataPair.first.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = dataPair.second.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}