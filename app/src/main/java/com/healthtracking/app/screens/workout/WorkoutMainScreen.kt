package com.healthtracking.app.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.entities.Workout
import com.healthtracking.app.screens.ScreenHeader
import com.healthtracking.app.viewmodels.database.WorkoutViewModel

@Composable
fun WorkoutMain(
    modifier: Modifier,
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
) {
    val workoutList by workoutViewModel.allWorkouts.observeAsState(initial = emptyList())

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenHeader(
            headerStringId = R.string.my_workouts_title,
            spacerSize = 16.dp
        )

        val innerModifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        if (workoutList.isEmpty()) {
            Text(modifier = innerModifier, text= stringResource(id = R.string.no_existing_exercises))
        } else {
            LazyColumn(
                modifier = innerModifier
            ) {
                itemsIndexed(workoutList) { _, workout ->
                    WorkoutCard(
                        modifier = Modifier.padding(bottom = 12.dp),
                        workout = workout,
                        deleteWorkout = { workoutViewModel.deleteWorkout(workout) },
                        editWorkout = { navController.navigate("EditWorkout/${workout.id}") },
                        runWorkout = {
                            navController.navigate("RunWorkout/${workout.id}")
                        }
                    )
                }
            }
        }

        Button(
            onClick = { navController.navigate("AddWorkout") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = stringResource(id = R.string.add_workout),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun WorkoutCard(
    modifier: Modifier = Modifier,
    workout: Workout,
    runWorkout: () -> Unit,
    deleteWorkout: () -> Unit,
    editWorkout: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        onClick = { runWorkout() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = workout.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    text = workout.description,
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = stringResource(id = R.string.view_options),
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.edit)) },
                        onClick = { editWorkout() }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.delete)) },
                        onClick = {
                            deleteWorkout()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}