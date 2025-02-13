package com.healthtracking.app.composables.screens.workout

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.healthtracking.app.composables.BackgroundBorderBox
import com.healthtracking.app.viewmodels.screen.BuildWorkoutViewModel
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
    viewModel: BuildWorkoutViewModel
) {
    Column(
        modifier = modifier.fillMaxSize().padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackgroundBorderBox {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                daysOfWeek.forEach { (day, stringResId) ->
                    ToggleDayButton(
                        dayName = stringResource(id = stringResId),
                        toggle = { viewModel.toggleScheduledDay(day) },
                        isSelected = { viewModel.scheduledDays.contains(day) }
                    )
                }
            }
        }

        Column(
            modifier = Modifier.weight(0.1f),
            verticalArrangement = Arrangement.Bottom
        ) {
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
        targetValue = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
        label = "AnimateColourChange",
    )

    Button(
        onClick = { toggle() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = MaterialTheme.colorScheme.onSecondary)
    ) {
        Text(text = dayName)
    }
}