package com.healthtracking.app.composables.graphs.eat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.graphs.generic.DatedLineChartWithYInterceptLine
import com.healthtracking.app.theme.CarbsColour
import com.healthtracking.app.theme.FatsColour
import com.healthtracking.app.theme.ProteinColour
import java.time.LocalDate
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

private const val ProteinValue: Int = 0
private const val CarbsValue: Int = 1
private const val FatsValue: Int = 2

@Composable
fun MacrosEatenGraph(
    proteinData: Map<LocalDate, Double>,
    carbsData: Map<LocalDate, Double>,
    fatsData: Map<LocalDate, Double>,
    proteinGoal: Double,
    carbsGoal: Double,
    fatsGoal: Double
) {
    val currentMacroChart = rememberSaveable() { mutableIntStateOf( 0 ) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.2f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MacroSelectionButton(
                text = stringResource(id = R.string.protein),
                onClick = { currentMacroChart.intValue = ProteinValue },
                isSelected = currentMacroChart.intValue == ProteinValue
            )
            MacroSelectionButton(
                text = stringResource(id = R.string.carbs),
                onClick = { currentMacroChart.intValue = CarbsValue },
                isSelected = currentMacroChart.intValue == CarbsValue
            )
            MacroSelectionButton(
                text = stringResource(id = R.string.fats),
                onClick = { currentMacroChart.intValue = FatsValue },
                isSelected = currentMacroChart.intValue == FatsValue
            )
        }

        Box(modifier = Modifier.weight(0.8f)) {
            when (currentMacroChart.intValue) {
                ProteinValue -> NutrientProgressGraph(
                    data = proteinData,
                    goal = proteinGoal,
                    goalLabel = stringResource(id = R.string.protein_intercept),
                    barColour = ProteinColour
                )

                CarbsValue -> NutrientProgressGraph(
                    data = carbsData,
                    goal = carbsGoal,
                    goalLabel = stringResource(id = R.string.carbs_intercept),
                    barColour = CarbsColour
                )

                FatsValue -> NutrientProgressGraph(
                    data = fatsData,
                    goal = fatsGoal,
                    goalLabel = stringResource(id = R.string.fats_intercept),
                    barColour = FatsColour
                )
            }
        }
    }
}

@Composable
private fun RowScope.MacroSelectionButton(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Button(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(4.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if (isSelected) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.tertiary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color =
                if (isSelected) MaterialTheme.colorScheme.onSecondary
                else MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Composable
internal fun NutrientProgressGraph(
    data: Map<LocalDate, Double>,
    goal: Double,
    goalLabel: String,
    barColour: Color
) {
    val stepSize = if (goal > 0) 20*(ceil(abs((goal)/(2 * 20)))) else 20.0

    DatedLineChartWithYInterceptLine(
        modifier = Modifier,
        stepSize = stepSize,
        lineColor = barColour,
        interceptLineLabel = goalLabel,
        interceptY = goal,
        data = data,
    )
}