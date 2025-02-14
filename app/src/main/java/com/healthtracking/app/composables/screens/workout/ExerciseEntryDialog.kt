package com.healthtracking.app.composables.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.CustomDialog
import com.healthtracking.app.composables.TextFieldWithErrorMessage

@Composable
internal fun ExerciseEntryDialog(
    saveEntry: () -> Boolean,
    clearEntry: () -> Unit,
    onDismissRequest: () -> Unit,
    hasSaved: Boolean,
    weight: String, // TODO make this and other lines dynamic later
    updateWeight: (String) -> Unit,
    validWeight: Boolean,
    reps: String,
    updateReps: (String) -> Unit,
    validReps: Boolean,
) {
    CustomDialog(onDismissRequest = onDismissRequest) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.exercise_entry_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Weight Input Field
            TextFieldWithErrorMessage(
                modifier = Modifier.fillMaxWidth(),
                value = weight,
                onValueChange = updateWeight,
                label = stringResource(id = R.string.weight_label),
                placeholder = stringResource(id = R.string.weight_label),
                hasSaved = hasSaved,
                hasError = !validWeight,
                errorMessage = stringResource(id = R.string.weight_error_message)
            )

            // Reps Input Field
            TextFieldWithErrorMessage(
                modifier = Modifier.fillMaxWidth(),
                value = reps,
                onValueChange = updateReps,
                label = stringResource(id = R.string.reps_label),
                placeholder = stringResource(id = R.string.reps_placeholder),
                hasSaved = hasSaved,
                hasError = !validReps,
                errorMessage = stringResource(id = R.string.reps_error_message)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick =
                {
                    if (saveEntry()) {onDismissRequest()}
                }) {
                    Text(
                        text = stringResource(id = R.string.add),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                TextButton(onClick = {
                    clearEntry()
                    onDismissRequest()
                }) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}