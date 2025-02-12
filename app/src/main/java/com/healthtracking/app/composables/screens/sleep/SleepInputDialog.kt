package com.healthtracking.app.composables.screens.sleep

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SleepInputDialog(
    title: String,
    state: TimePickerState,
    onDismissRequest: () -> Unit,
    colors: TimePickerColors
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
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
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )

                TimePicker(state = state, colors = colors)

                TextButton(
                    modifier = Modifier.align(alignment = Alignment.End),
                    onClick = onDismissRequest)
                {
                    Text(stringResource(id = R.string.close))
                }
            }
        }
    }
}