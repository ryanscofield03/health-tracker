package com.healthtracking.app.composables.screens.workout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.composables.HeaderAndListBox
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.composables.TextFieldWithErrorMessage
import com.healthtracking.app.entities.Metric
import com.healthtracking.app.services.getExerciseList
import com.healthtracking.app.theme.CustomCutCornerShape
import com.healthtracking.app.viewmodels.screen.BuildWorkoutViewModel

@Composable
fun AddExercise(
    modifier: Modifier,
    navController: NavController,
    viewModel: BuildWorkoutViewModel
) {
    val context = LocalContext.current

    // should ideally be in viewmodel
    val exerciseGroups = stringArrayResource(id = R.array.exercise_groups)
    val selectedExerciseGroup = rememberSaveable { mutableStateOf(exerciseGroups[0]) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AddNewExerciseButton(
                modifier = Modifier.weight(0.2f),
                addNewExercise = {
                    if (viewModel.isExerciseSearchValid()) {
                        viewModel.addExercise(exercise = Exercise(
                            name = viewModel.exerciseSearch,
                            metrics = listOf(Metric.REPS, Metric.WEIGHT)
                        ))
                        viewModel.clearExerciseScreen()
                    } else {
                        viewModel.failedToAddExercise()
                    }
                }
            )

            TextFieldWithErrorMessage(
                modifier = Modifier.weight(1f),
                value = viewModel.exerciseSearch,
                onValueChange = { viewModel.updateExerciseSearch(it) },
                label = stringResource(id = R.string.exercise_search_label),
                placeholder = stringResource(id = R.string.exercise_search_placeholder),
                hasSaved = true,
                hasError = viewModel.alreadyAddedExercise,
                errorMessage = stringResource(id = R.string.exercise_name_search_invalid)
            )
        }

        AnimatedContent(
            targetState = selectedExerciseGroup,
            label = "AnimateChangePage",
            transitionSpec = {
                (slideInHorizontally { it } + fadeIn())
                    .togetherWith(slideOutVertically { -it } + fadeOut())
            }
        ) { currentGroup ->
            var exerciseList = remember(currentGroup.value, viewModel.exerciseSearch) {
                getExerciseList(context, currentGroup.value)
                    ?.filter { it.contains(viewModel.exerciseSearch) } ?: emptyList()
            }
            if (currentGroup.value == "Selected") {
                exerciseList = exerciseList.plus(viewModel.exercises.map { it.name })
            }

            HeaderAndListBox(
                modifier = Modifier.fillMaxHeight(0.85f),
                header = {
                    ExerciseCategorySelector(
                        exerciseGroups = exerciseGroups,
                        selectedExerciseGroup = selectedExerciseGroup
                    )
                },
                listContent = {
                    ExerciseList(
                        exerciseList = exerciseList,
                        selectedExercises = viewModel.exercises,
                        addExercise = { viewModel.addExercise(it)},
                        removeExercise = { viewModel.removeExercise(it) }
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                onClick = { navController.popBackStack(); viewModel.clearExerciseScreen() }
            ) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    }
}

@Composable
private fun AddNewExerciseButton(
    modifier: Modifier = Modifier,
    addNewExercise: () -> Unit
) {
    IconButton(
        modifier = modifier
            .fillMaxHeight()
            .padding(top = 8.dp, bottom = 15.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.tertiary),
        onClick = addNewExercise
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add_exercise)
        )
    }
}

@Composable
private fun ExerciseList(
    exerciseList: List<String>,
    selectedExercises: List<Exercise>,
    removeExercise: (Exercise) -> Unit,
    addExercise: (Exercise) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(exerciseList) { _, exerciseName: String ->
            val isSelected = selectedExercises.any { it.name == exerciseName }
            ExerciseDisplayItem(
                name = exerciseName,
                onClick = {
                    if (isSelected) {
                        removeExercise(selectedExercises.find { it.name == exerciseName }!!)
                    } else {
                        addExercise(Exercise(name = exerciseName, metrics = listOf(Metric.REPS, Metric.WEIGHT)))
                    }
                },
                isCurrentlySelected = isSelected
            )
        }
    }
}

@Composable
private fun ExerciseCategorySelector(
    exerciseGroups: Array<String>,
    selectedExerciseGroup: MutableState<String>,

) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(exerciseGroups) { _, exerciseGroup ->
            Button(
                modifier = Modifier.padding(vertical = 8.dp),
                onClick = { selectedExerciseGroup.value = exerciseGroup },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                    if (selectedExerciseGroup.value == exerciseGroup)
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
}

@Composable
private fun ExerciseDisplayItem(
    name: String,
    onClick: (String) -> Unit,
    isCurrentlySelected: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(65.dp)
            .clickable(onClick = { onClick(name) }),
        shape = CustomCutCornerShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector =
                if (isCurrentlySelected) Icons.Default.CheckCircle
                else Icons.Outlined.CheckCircle,
                contentDescription = stringResource(id = R.string.add),
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}