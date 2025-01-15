package com.healthtracking.app.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.healthtracking.app.viewmodels.screen.SettingsViewModel

@Composable
fun SettingsMain (
    modifier: Modifier,
    settingsViewModel: SettingsViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "TEST",
            style = MaterialTheme.typography.displaySmall
        )

        LanguagePicker()

        ToggleMeasurementType()

        ToggleNotifications()

        HelpBox()
    }
}

@Composable
fun LanguagePicker() {
    TODO("Not yet implemented")
}

@Composable
fun ToggleMeasurementType() {
    TODO("Not yet implemented")
}

@Composable
fun ToggleNotifications() {
    TODO("Not yet implemented")
}

@Composable
fun HelpBox() {
    TODO("Not yet implemented")
}