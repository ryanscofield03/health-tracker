package nz.ac.canterbury.seng303.healthtracking.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.healthtracking.R
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.screens.ErrorMessageComponent
import nz.ac.canterbury.seng303.healthtracking.screens.SaveAndCancelButtons
import nz.ac.canterbury.seng303.healthtracking.viewmodels.screen.AddWorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExercise(
    modifier: Modifier,
    navController: NavController,
    viewModel: AddWorkoutViewModel
) {
    var name by rememberSaveable {
        mutableStateOf("")
    }
    var isError by rememberSaveable {
        mutableStateOf(false)
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