package nz.ac.canterbury.seng303.healthtracking.screens.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
            .fillMaxHeight(0.88f)
            .padding(8.dp)
    ) {
        // Exercise Name
        Text(
            text = exercise.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(10.dp))

        val tableData = (1..4).mapIndexed { index, item ->
            index to "Item $index"
        }
        LazyColumn {
            item {
                Row(Modifier.background(MaterialTheme.colorScheme.tertiary)) {
                    TableCell(text = "Weight")
                    TableCell(text = "Reps")
                    TableCell(text = "Weight")
                    TableCell(text = "Reps")
                }
            }
            items(tableData) {
                val (id, text) = it
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = id.toString())
                    TableCell(text = text)
                    TableCell(text = "...")
                    TableCell(text = "...")
                }
            }
        }

        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(text = stringResource(id = R.string.add_exercise_entry))
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
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