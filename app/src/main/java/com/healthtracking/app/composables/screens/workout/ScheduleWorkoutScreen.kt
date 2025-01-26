package com.healthtracking.app.composables.screens.workout

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.composables.ScreenHeader
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
                onClick = { navController.popBackStack() },
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
    val selected = isSelected()

    // Use animateColorAsState to smoothly animate color changes based on the state
    val containerColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
        label = "AnimateColourChange",
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onSecondary
        else MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.5f),
        label = "AnimateColourChange"
    )

    Button(
        onClick = { toggle() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor)
    ) {
        Text(text = dayName)
    }
}