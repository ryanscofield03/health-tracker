package com.healthtracking.app.composables.screens.workout

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.composables.HeaderAndListBox
import com.healthtracking.app.entities.Workout
import com.healthtracking.app.theme.CustomCutCornerShape
import com.healthtracking.app.viewmodels.database.WorkoutViewModel

@Composable
fun WorkoutMain(
    modifier: Modifier,
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
) {
    val workoutList by workoutViewModel.allWorkouts.observeAsState(initial = emptyList())

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxHeight(0.9f)) {
            HeaderAndListBox(
                modifier = Modifier.align(Alignment.TopCenter),
                header = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.my_workouts_title),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                listContent = {
                    WorkoutCards(
                        workoutList = workoutList,
                        deleteWorkout = { workoutViewModel.deleteWorkout(it) },
                        navController = navController
                    )
                },
                contentPlaceholderText = stringResource(id = R.string.no_existing_workouts),
                isContentEmpty = workoutList.isEmpty()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
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
}

@Composable
private fun WorkoutCards(
    workoutList: List<Workout>,
    deleteWorkout: (workout: Workout) -> Unit,
    navController: NavController
) {
    LazyColumn {
        itemsIndexed(workoutList) { _, workout ->
            WorkoutCard(
                modifier = Modifier.padding(bottom = 12.dp),
                workout = workout,
                deleteWorkout = { deleteWorkout(workout) },
                editWorkout = { navController.navigate("EditWorkout/${workout.id}") },
                runWorkout = {
                    navController.navigate("RunWorkout/${workout.id}")
                }
            )
        }
    }
}

@Composable
private fun WorkoutCard(
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
        shape = CustomCutCornerShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        elevation = CardDefaults.cardElevation(4.dp),
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