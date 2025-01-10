package nz.ac.canterbury.seng303.healthtracking.screens.workout

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.healthtracking.R
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.screens.SaveAndCancelButtons
import nz.ac.canterbury.seng303.healthtracking.services.getExerciseList
import nz.ac.canterbury.seng303.healthtracking.viewmodels.screen.AddWorkoutViewModel

@Composable
fun AddExercise(
    modifier: Modifier,
    navController: NavController,
    viewModel: AddWorkoutViewModel
) {
    val context = LocalContext.current
    val exerciseGroups = stringArrayResource(id = R.array.exercise_groups)

    var selectedExerciseGroup by rememberSaveable {
        mutableStateOf(exerciseGroups[0])
    }
    val selectedExercises = rememberSaveable (
        saver = listSaver(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    ){
        viewModel.exercises.map { it.name }.toMutableStateList()
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.add_exercise),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(exerciseGroups) { _, exerciseGroup ->
                Button(
                    modifier = Modifier.padding(vertical = 7.dp),
                    onClick = { selectedExerciseGroup = exerciseGroup },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (selectedExerciseGroup == exerciseGroup)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text = exerciseGroup)
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxHeight(0.9f)) {
            val exerciseList = getExerciseList(context, selectedExerciseGroup)
            if (exerciseList != null) {
                itemsIndexed(exerciseList) { _, exerciseName: String ->
                    ExerciseDisplayItem(
                        name = exerciseName,
                        onClick = {
                            if (selectedExercises.contains(exerciseName)) {
                                selectedExercises.removeAll{it == exerciseName}
                            } else {
                                selectedExercises.add(exerciseName)
                            }
                        },
                        isCurrentlySelected = selectedExercises.contains(exerciseName)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            SaveAndCancelButtons(
                onSave = {
                    viewModel.clearExercises()
                    selectedExercises.forEach { exerciseName ->
                        viewModel.addExercise(Exercise(name = exerciseName))
                    }
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun ExerciseDisplayItem(
    name: String,
    onClick: (String) -> Unit,
    isCurrentlySelected: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(65.dp)
            .padding(4.dp)
            .clickable(onClick = { onClick(name) }),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Icon(
                imageVector = if (isCurrentlySelected) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                contentDescription = stringResource(id = R.string.add),
                tint = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
            )
        }
    }
}