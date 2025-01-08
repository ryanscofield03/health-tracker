package nz.ac.canterbury.seng303.healthtracking.screens.workout

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import nz.ac.canterbury.seng303.healthtracking.screens.ErrorMessageComponent
import nz.ac.canterbury.seng303.healthtracking.screens.SaveAndCancelButtons
import nz.ac.canterbury.seng303.healthtracking.services.getExerciseList
import nz.ac.canterbury.seng303.healthtracking.viewmodels.screen.AddWorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExercise(
    modifier: Modifier,
    navController: NavController,
    viewModel: AddWorkoutViewModel
) {
    val context = LocalContext.current

    var name by rememberSaveable {
        mutableStateOf("")
    }
    var isError by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedExerciseGroup by rememberSaveable {
        mutableStateOf("")
    }
    var selectedItem by rememberSaveable {
        mutableStateOf("")
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
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it; if (name.isNotBlank()) isError = false },
            label = { Text(stringResource(id = R.string.exercise_name)) },
            placeholder = { Text(stringResource(id = R.string.exercise_search_placeholder)) },
            maxLines = 1,
            isError = isError,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                errorLabelColor = MaterialTheme.colorScheme.onBackground
            )
        )

        ErrorMessageComponent(hasError = isError, errorMessageId = R.string.exercise_error_message)

        Text(
            text = stringResource(id = R.string.search_exercise),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(10.dp))

        val exerciseGroups = stringArrayResource(id = R.array.exercise_groups)
        LazyRow {
            itemsIndexed(exerciseGroups) { _, exerciseGroup ->
                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = { selectedExerciseGroup = exerciseGroup },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedExerciseGroup == exerciseGroup) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(text = exerciseGroup)
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxHeight(0.8f)) {
            val exerciseList = getExerciseList(context, selectedExerciseGroup)
            if (exerciseList != null) {
                itemsIndexed(exerciseList) { _, item ->
                    ExerciseDisplayItem(
                        name = item,
                        onClick = { name = item; selectedItem = item },
                        isCurrentlySelected = item == selectedItem)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            SaveAndCancelButtons(
                onSave = {
                    if (name.isNotBlank()) {
                        val exercise = Exercise(name = name)
                        viewModel.addExercise(exercise)
                        navController.popBackStack()
                    } else {
                        isError = true
                    }
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
            .height(70.dp)
            .padding(4.dp)
            .clickable(onClick = { onClick(name) }),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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