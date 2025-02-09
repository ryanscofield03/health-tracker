package com.healthtracking.app.composables.graphs.eat

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.healthtracking.app.R
import com.healthtracking.app.composables.graphs.generic.DatedLineChartWithYInterceptLine
import com.healthtracking.app.theme.CaloriesColour
import java.time.LocalDate

@Composable
fun CaloriesEatenGraph(
    caloriesData: Map<LocalDate, Double>
) {
    if (caloriesData.values.isEmpty()) {
        Text(text = "Insufficient data")
    } else {
        DatedLineChartWithYInterceptLine(
            modifier = Modifier,
            stepSize = 500.0,
            lineColor = CaloriesColour,
            interceptLineLabel = stringResource(id = R.string.calories_intercept),
            interceptY = 2500.0,
            data = caloriesData
        )
    }
}