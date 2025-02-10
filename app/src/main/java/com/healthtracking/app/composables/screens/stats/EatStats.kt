package com.healthtracking.app.composables.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.BackgroundBorderBox
import com.healthtracking.app.composables.graphs.eat.MacrosEatenGraph
import com.healthtracking.app.composables.graphs.eat.NutrientProgressGraph
import com.healthtracking.app.theme.CaloriesColour
import java.time.LocalDate

@Composable
fun EatStats(
    caloriesData: Map<LocalDate, Double>,
    proteinData: Map<LocalDate, Double>,
    carbsData: Map<LocalDate, Double>,
    fatsData: Map<LocalDate, Double>,
    caloriesGoal: Double,
    proteinGoal: Double,
    carbsGoal: Double,
    fatsGoal: Double
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BackgroundBorderBox(Modifier.weight(0.5f).fillMaxWidth()) {
            // graph of calories eaten
            NutrientProgressGraph(
                data = caloriesData,
                goal = caloriesGoal,
                goalLabel = stringResource(id = R.string.calories_intercept),
                barColour = CaloriesColour
            )
        }

        BackgroundBorderBox(Modifier.weight(0.5f).fillMaxWidth()) {
            // graph of each macro in comparison to goal (x3)
            MacrosEatenGraph(
                proteinData = proteinData,
                carbsData = carbsData,
                fatsData = fatsData,
                proteinGoal = proteinGoal,
                carbsGoal = carbsGoal,
                fatsGoal = fatsGoal
            )
        }
    }
}