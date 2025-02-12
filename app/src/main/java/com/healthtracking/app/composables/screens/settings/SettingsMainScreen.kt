package com.healthtracking.app.composables.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.healthtracking.app.R
import com.healthtracking.app.composables.BackgroundBorderBox
import com.healthtracking.app.composables.HeaderAndListBox
import com.healthtracking.app.theme.CustomCutCornerShape
import com.healthtracking.app.viewmodels.screen.SettingsViewModel

@Composable
fun SettingsMain (
    modifier: Modifier,
    viewModel: SettingsViewModel
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        LanguagePicker(
            languageSetting = viewModel.languageSetting.collectAsStateWithLifecycle().value,
            changeLanguageSetting = { viewModel.saveLanguageSetting(it) }
        )
        ToggleMeasurementType(
            measurementsSetting = viewModel.measurementsSetting.collectAsStateWithLifecycle().value,
            toMetric = { viewModel.saveMeasurementsSetting(SettingsViewModel.MEASUREMENTS_METRIC) },
            toImperial = { viewModel.saveMeasurementsSetting(SettingsViewModel.MEASUREMENTS_IMPERIAL) }
        )
        ToggleNotifications(
            notificationsSetting = viewModel.notificationsSetting.collectAsStateWithLifecycle().value,
            changeNotificationsSetting = { viewModel.saveNotificationsSetting() }
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            HelpBox()
        }
    }
}

@Composable
fun LanguagePicker(
    languageSetting: String,
    changeLanguageSetting: (String) -> Unit
) {
    val languages = stringArrayResource(id = R.array.languages)

    HeaderAndListBox(
        modifier = Modifier.fillMaxHeight(0.2f),
        header = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.select_language_settings),
                style = MaterialTheme.typography.titleMedium
            )
        },
        listContent = {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(languages) { language ->
                    Button(
                        onClick = { changeLanguageSetting(language) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (languageSetting == language)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        ),
                        shape = CustomCutCornerShape
                    ) {
                        Text(text = language)
                    }
                }
            }
        }
    )
}

@Composable
fun ToggleMeasurementType(
    measurementsSetting: String,
    toMetric: () -> Unit,
    toImperial: () -> Unit,
) {
    BackgroundBorderBox {
        Column {
            Text(
                text = stringResource(id = R.string.select_measurement_settings),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier.weight(0.5f),
                    shape = CustomCutCornerShape,
                    onClick = {
                        toMetric()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                        if (measurementsSetting == "metric")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                    )
                ) {
                    Text(text = stringResource(id = R.string.metric))
                }
                Button(
                    modifier = Modifier.weight(0.5f),
                    shape = CustomCutCornerShape,
                    onClick = {
                        toImperial()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                        if (measurementsSetting == "imperial")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.imperial)
                    )
                }
            }
        }
    }
}

@Composable
fun ToggleNotifications(
    notificationsSetting: Boolean,
    changeNotificationsSetting: () -> Unit
) {
    BackgroundBorderBox {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.select_notification),
                style = MaterialTheme.typography.titleMedium
            )
            Switch(checked = notificationsSetting, onCheckedChange = { changeNotificationsSetting() })
        }
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