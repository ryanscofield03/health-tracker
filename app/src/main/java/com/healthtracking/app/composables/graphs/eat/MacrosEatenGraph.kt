package com.healthtracking.app.composables.graphs.eat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.graphs.generic.DatedLineChartWithYInterceptLine
import com.healthtracking.app.ui.theme.CarbsColour
import com.healthtracking.app.ui.theme.FatsColour
import com.healthtracking.app.ui.theme.ProteinColour
import java.time.LocalDate

private const val ProteinValue: Int = 0
private const val CarbsValue: Int = 1
private const val FatsValue: Int = 2

@Composable
fun MacrosEatenGraph(
    proteinData: Map<LocalDate, Double>,
    carbsData: Map<LocalDate, Double>,
    fatsData: Map<LocalDate, Double>
) {
    val currentMacroChart = rememberSaveable() { mutableIntStateOf( 0 ) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.2f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MacroSelectionButton(
                text = stringResource(id = R.string.protein_letter),
                onClick = { currentMacroChart.intValue = ProteinValue },
                isSelected = currentMacroChart.intValue == ProteinValue
            )
            MacroSelectionButton(
                text = stringResource(id = R.string.carbs_letter),
                onClick = { currentMacroChart.intValue = CarbsValue },
                isSelected = currentMacroChart.intValue == CarbsValue
            )
            MacroSelectionButton(
                text = stringResource(id = R.string.fats_letter),
                onClick = { currentMacroChart.intValue = FatsValue },
                isSelected = currentMacroChart.intValue == FatsValue
            )
        }

        Box(modifier = Modifier.weight(0.8f)) {
            when (currentMacroChart.intValue) {
                ProteinValue -> ProteinProgressGraph(
                    data = proteinData,
                    proteinGoal = 100.0
                )

                CarbsValue -> CarbsProgressGraph(
                    data = carbsData,
                    carbsGoal = 400.0
                )

                FatsValue -> FatsProgressGraph(
                    data = fatsData,
                    fatsGoal = 40.0
                )
            }
        }
    }
}

@Composable
fun ColumnScope.MacroSelectionButton(
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
            text = text
        )
    }
}

@Composable
fun FatsProgressGraph(
    data: Map<LocalDate, Double>,
    fatsGoal: Double
) {
    DatedLineChartWithYInterceptLine(
        modifier = Modifier,
        stepSize = 10.0,
        lineColor = FatsColour,
        interceptLineLabel = stringResource(id = R.string.fats_intercept),
        interceptY = fatsGoal,
        data = data,
    )
}

@Composable
fun CarbsProgressGraph(
    data: Map<LocalDate, Double>,
    carbsGoal: Double
) {
    DatedLineChartWithYInterceptLine(
        modifier = Modifier,
        stepSize = 100.0,
        lineColor = CarbsColour,
        interceptLineLabel = stringResource(id = R.string.carbs_intercept),
        interceptY = carbsGoal,
        data = data,
    )
}

@Composable
fun ProteinProgressGraph(
    data: Map<LocalDate, Double>,
    proteinGoal: Double
) {
    DatedLineChartWithYInterceptLine(
        modifier = Modifier,
        stepSize = 20.0,
        lineColor = ProteinColour,
        interceptLineLabel = stringResource(id = R.string.protein_intercept),
        interceptY = proteinGoal,
        data = data,
    )
}