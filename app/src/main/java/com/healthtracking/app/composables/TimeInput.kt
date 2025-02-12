package com.healthtracking.app.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Custom time input that works and looks similar to Material3 TimePicker but
 * allows the user to disable the timepicker, and not auto select on screen open
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TimeInputDisplay(
    modifier: Modifier = Modifier,
    title: String,
    state: TimePickerState,
    colors: TimePickerColors,
    enabled: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            title,
            color = if (enabled) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Hour Picker
            TimePickerValueDisplay(
                value = state.hour,
                minusTwelveHours = state.isAfternoon && state.hour != 12,
                colors = colors,
                enabled = enabled
            )

            Text(
                text = ":",
                modifier = Modifier.padding(horizontal = 4.dp),
                style = TextStyle(
                    color = if (enabled) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = MaterialTheme.typography.displayMedium.fontSize
                ),
            )

            // Minute Picker
            TimePickerValueDisplay(
                value = state.minute,
                colors = colors,
                enabled = enabled
            )

            Spacer(Modifier.width(10.dp))

            PeriodPickerDisplay(
                enabled = enabled,
                colors = colors,
                isAfternoon = state.isAfternoon
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PeriodPickerDisplay(
    enabled: Boolean,
    colors: TimePickerColors,
    isAfternoon: Boolean
) {
    Column (
        modifier = Modifier
            .clip(RoundedCornerShape(15))
            .height(70.dp)
            .width(53.dp)
            .border(
                width = 1.dp,
                color = colors.periodSelectorBorderColor,
                shape = RoundedCornerShape(15)
            )
    ) {
        PeriodButtonDisplay(
            enabled = enabled,
            selected = !isAfternoon,
            colors = colors,
            periodText = "a.m."
        )
        HorizontalDivider(thickness = 1.dp, color = colors.periodSelectorBorderColor)
        PeriodButtonDisplay(
            enabled = enabled,
            selected = isAfternoon,
            colors = colors,
            periodText = "p.m."
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColumnScope.PeriodButtonDisplay(
    enabled: Boolean,
    selected: Boolean,
    colors: TimePickerColors,
    periodText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(
                color = if (!enabled && selected) MaterialTheme.colorScheme.surfaceVariant
                else if (!enabled) MaterialTheme.colorScheme.surface
                else if (selected) colors.periodSelectorSelectedContainerColor
                else colors.periodSelectorUnselectedContainerColor,
                shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp)
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = periodText,
            color = if (!enabled) MaterialTheme.colorScheme.onSurfaceVariant
            else if (selected) colors.periodSelectorSelectedContentColor
            else colors.periodSelectorUnselectedContentColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerValueDisplay(
    value: Int,
    minusTwelveHours: Boolean = false,
    colors: TimePickerColors,
    enabled: Boolean
) {
    val shape = RoundedCornerShape(5.dp)

    Box(
        modifier = Modifier
            .width(90.dp)
            .height(70.dp)
            .background(
                color = if (!enabled) MaterialTheme.colorScheme.surfaceVariant
                else colors.timeSelectorUnselectedContainerColor,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = (value - if (minusTwelveHours) 12 else 0).toString().padStart(2, '0'),
            style = TextStyle(
                color = if (!enabled) MaterialTheme.colorScheme.onSurfaceVariant
                else colors.timeSelectorUnselectedContentColor,
                fontSize = MaterialTheme.typography.displayMedium.fontSize
            )
        )
    }
}