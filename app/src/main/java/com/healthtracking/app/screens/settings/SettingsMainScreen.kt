package com.healthtracking.app.screens.settings

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.viewmodels.screen.SettingsViewModel

@Composable
fun SettingsMain (
    modifier: Modifier,
    viewModel: SettingsViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.settings_screen),
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.height(16.dp))
        LanguagePicker()
        Spacer(modifier = Modifier.height(48.dp))
        ToggleMeasurementType(
            measurements = viewModel.measurements,
            changeMeasurements = { viewModel.saveMeasurements(it) }
        )
        Spacer(modifier = Modifier.height(48.dp))
        ToggleNotifications()
        Spacer(modifier = Modifier.height(48.dp))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            HelpBox()
        }
    }
}

@Composable
fun LanguagePicker() {
    val languages = stringArrayResource(id = R.array.languages)
    
    Text(
        text = stringResource(id = R.string.select_language),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(languages) { language ->
            Button(onClick = { /*TODO*/ }, shape = RoundedCornerShape(0.dp)) {
                Text(text = language)
            }
        }
    }
}

@Composable
fun ToggleMeasurementType(
    measurements: String,
    changeMeasurements: (String) -> Unit
) {
    Text(
        text = stringResource(id = R.string.select_measurement),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val metric = "metric"
        val imperial = "imperial"

        Button(
            modifier = Modifier.weight(0.5f),
            shape = RoundedCornerShape(0.dp),
            onClick = {
                changeMeasurements(metric)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (measurements == metric)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
            )
        ) {
            Text(text = stringResource(id = R.string.metric))
        }
        Button(
            modifier = Modifier.weight(0.5f),
            shape = RoundedCornerShape(0.dp),
            onClick = {
                changeMeasurements(imperial)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (measurements == imperial)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = stringResource(id = R.string.imperial)
            )
        }
    }
}

@Composable
fun ToggleNotifications() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.select_notification),
            style = MaterialTheme.typography.titleLarge
        )
        Switch(checked = true, onCheckedChange = {})
    }
}

@Composable
fun HelpBox() {
    Text(
        text = stringResource(id = R.string.ask_for_help),
        style = TextStyle(
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            color = Color.Blue,
            textDecoration = TextDecoration.Underline
        ),
        modifier = Modifier.clickable {
            /* TODO */
        }
    )
}