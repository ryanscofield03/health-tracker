package com.healthtracking.app.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.screens.ScreenHeader
import com.healthtracking.app.viewmodels.screen.AddWorkoutViewModel
import java.time.DayOfWeek

val daysOfWeek = listOf(
    DayOfWeek.MONDAY to R.string.monday,
    DayOfWeek.TUESDAY to R.string.tuesday,
    DayOfWeek.WEDNESDAY to R.string.wednesday,
    DayOfWeek.THURSDAY to R.string.thursday,
    DayOfWeek.FRIDAY to R.string.friday,
    DayOfWeek.SATURDAY to R.string.saturday,
    DayOfWeek.SUNDAY to R.string.sunday
)

@Composable
fun ScheduleWorkout(
    modifier: Modifier,
    navController: NavController,
    viewModel: AddWorkoutViewModel
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenHeader(
            headerStringId = R.string.schedule_workout,
            spacerSize = 16.dp
        )

        daysOfWeek.forEach { (day, stringResId) ->
            ToggleDayButton(
                dayName = stringResource(id = stringResId),
                toggle = { viewModel.toggleScheduledDay(day) },
                isSelected = { viewModel.scheduledDays.contains(day) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
            Button(
                onClick = { navController.navigate("addWorkout") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(stringResource(id = R.string.save_schedule))
            }
        }
    }
}

@Composable
fun ToggleDayButton(
    dayName: String,
    toggle: () -> Unit,
    isSelected: () -> Boolean
){
    Button(
        onClick = { toggle() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected()) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f),
            contentColor = if (isSelected()) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.6f)
        )) {
        Text(text = dayName)
    }
}