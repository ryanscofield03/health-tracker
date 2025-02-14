package com.healthtracking.app.composables.screens.eat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.CustomDialog

@Composable
internal fun MacroGoalDialog(
    isOpen: Boolean,
    onSubmit: () -> Boolean,
    onDismissRequest: () -> Unit,
    caloriesGoal: String,
    proteinGoal: Float,
    updateProteinGoal: (Float) -> Unit,
    carbsGoal: Float,
    updateCarbsGoal: (Float) -> Unit,
    fatsGoal: Float,
    updateFatsGoal: (Float) -> Unit,
    maxProtein: Float,
    maxCarbs: Float,
    maxFats: Float
) {
    CustomDialog(isOpen = isOpen, onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.update_daily_food_goal),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = stringResource(id = R.string.dialog_calories_display, caloriesGoal),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            MacroPieChart(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                protein = proteinGoal,
                carbs = carbsGoal,
                fats = fatsGoal,
                updateProtein = { updateProteinGoal(it) },
                updateCarbs = { updateCarbsGoal(it) },
                updateFats = { updateFatsGoal(it) },
                maxProtein = maxProtein,
                maxCarbs = maxCarbs,
                maxFats = maxFats
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    if (onSubmit()) {
                        onDismissRequest()
                    }
                }) {
                    Text(
                        text = stringResource(id = R.string.save),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                TextButton(onClick = { onDismissRequest() }) {
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
