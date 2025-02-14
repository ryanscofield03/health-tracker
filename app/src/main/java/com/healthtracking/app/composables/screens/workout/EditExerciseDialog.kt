package com.healthtracking.app.composables.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.CustomDialog
import com.healthtracking.app.composables.HeaderAndListBox
import com.healthtracking.app.entities.Metric
import java.util.Locale

val metricOptions = listOf(
    listOf(Metric.WEIGHT, Metric.REPS),
    listOf(Metric.DISTANCE, Metric.TIME),
    listOf(Metric.DISTANCE, Metric.REPS),
    listOf(Metric.WEIGHT, Metric.TIME)
)

@Composable
fun EditExerciseDialog(
    isOpen: Boolean,
    onSave: () -> Unit,
    onDismissRequest: () -> Unit,
    exerciseName: String,
    exerciseMetrics: List<Metric>,
    updateMetrics: (List<Metric>) -> Unit
) {
    CustomDialog(
        isOpen = isOpen,
        onDismissRequest = onDismissRequest
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            HeaderAndListBox(
                modifier = Modifier.fillMaxHeight(0.5f),
                header = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = exerciseName,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Start
                    )
                },
                listContent = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        metricOptions.forEach { metricsList ->
                            MetricOptionBox(
                                isSelected = metricsList.containsAll(exerciseMetrics),
                                metrics = metricsList,
                                onClick = { updateMetrics(metricsList) }
                            )
                        }
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { onSave(); onDismissRequest() },
                ) {
                    Text(
                        text = stringResource(id = R.string.save),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                TextButton(
                    onClick = { onDismissRequest() },
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricOptionBox(
    isSelected: Boolean,
    metrics: List<Metric>,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = metrics[0].name.lowercase(Locale.ROOT)
                    .replaceFirstChar { it.titlecase(Locale.ROOT) },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = metrics[1].name.lowercase(Locale.ROOT)
                    .replaceFirstChar { it.titlecase(Locale.ROOT) },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
