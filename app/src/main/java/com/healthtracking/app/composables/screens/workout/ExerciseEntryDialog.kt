package com.healthtracking.app.composables.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.TextFieldWithErrorMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExerciseEntryDialog(
    saveEntry: () -> Boolean,
    clearEntry: () -> Unit,
    onDismiss: () -> Unit,
    weight: String, // TODO make this and other lines dynamic later
    updateWeight: (String) -> Unit,
    validWeight: Boolean,
    weightErrorMessageId: Int,
    reps: String,
    updateReps: (String) -> Unit,
    validReps: Boolean,
    repsErrorMessageId: Int
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
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
                    labelId = R.string.weight_label,
                    placeholderId = R.string.weight_label,
                    hasError = !validWeight,
                    errorMessageId = weightErrorMessageId
                )

                // Reps Input Field
                TextFieldWithErrorMessage(
                    modifier = Modifier.fillMaxWidth(),
                    value = reps,
                    onValueChange = updateReps,
                    labelId = R.string.reps_label,
                    placeholderId = R.string.reps_placeholder,
                    hasError = !validReps,
                    errorMessageId = repsErrorMessageId
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick =
                    {
                        if (saveEntry()) {onDismiss()}
                    }) {
                        Text(
                            text = stringResource(id = R.string.add),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    TextButton(onClick = {
                        clearEntry()
                        onDismiss()
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
}