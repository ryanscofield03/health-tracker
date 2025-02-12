package com.healthtracking.app.composables.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.CustomDialog
import com.healthtracking.app.composables.SelectionDropDown
import com.healthtracking.app.composables.SliderWithLabel
import com.healthtracking.app.composables.TextFieldWithErrorMessage
import com.healthtracking.app.theme.CarbsColour
import com.healthtracking.app.theme.FatsColour
import com.healthtracking.app.theme.ProteinColour
import java.util.Locale

@Composable
internal fun FoodEntryDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    name: String?,
    measurement: String,
    protein: Float,
    carbs: Float,
    fats: Float,
    quantity: Float,
    calories: Float,
    updateName: (String) -> Unit,
    updateMeasurement: (String) -> Unit,
    updateProtein: (Float) -> Unit,
    updateCarbs: (Float) -> Unit,
    updateFats: (Float) -> Unit,
    updateQuantity: (Float) -> Unit,
    measurementOptions: List<String>,
    nameHasError: Boolean
) {
    if (isOpen) {
        CustomDialog(
            modifier = modifier,
            onDismissRequest = onDismissRequest
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // food name
                TextFieldWithErrorMessage(
                    value = name,
                    onValueChange = updateName,
                    labelId = R.string.food_name_label,
                    placeholderId = R.string.food_name_placeholder,
                    hasError = nameHasError,
                    errorMessageId = R.string.food_name_error_message
                )

                // measurement dropdown
                MeasurementDropdown(
                    measurementOptions = measurementOptions,
                    measurement = measurement,
                    updateMeasurement = updateMeasurement
                )

                Spacer(modifier = Modifier.height(24.dp))

                // display calculation of calories
                Text(
                    text = "Total calories: ${String.format(Locale.US, "%.0f", calories.times(quantity))}kcal",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // protein slider
                SliderWithLabel(
                    label = stringResource(id = R.string.protein_formatted, String.format(Locale.US, "%.1f", protein)),
                    value = protein,
                    color = ProteinColour,
                    onValueChange = updateProtein,
                    maxSliderValue = 100f,
                    incrementAmount = 0.2f
                )

                // carbs slider
                SliderWithLabel(
                    label = stringResource(id = R.string.carbs_formatted, String.format(Locale.US, "%.1f", carbs)),
                    value = carbs,
                    color = CarbsColour,
                    onValueChange = updateCarbs,
                    maxSliderValue = 100f,
                    incrementAmount = 0.2f
                )

                // fats slider
                SliderWithLabel(
                    label = stringResource(id = R.string.fats_formatted, String.format(Locale.US, "%.1f", fats)),
                    value = fats,
                    color = FatsColour,
                    onValueChange = updateFats,
                    maxSliderValue = 100f,
                    incrementAmount = 0.2f
                )

                // quantity slider
                SliderWithLabel(
                    label = stringResource(id = R.string.quantity_formatted, String.format(
                        Locale.US, "%.0f", quantity)),
                    value = quantity,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    onValueChange = updateQuantity,
                    maxSliderValue = 10f
                )

                // save and cancel buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onSave
                    ) {
                        Text(
                            text = "Save",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    TextButton(onClick = {
                        onDismissRequest()
                    }) {
                        Text(
                            text = "Cancel",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MeasurementDropdown(
    measurement: String,
    measurementOptions: List<String>,
    updateMeasurement: (String) -> Unit
) {
    SelectionDropDown(
        label = stringResource(id = R.string.select_measurement_settings),
        items = measurementOptions,
        selectedText = measurement,
        onItemClick = updateMeasurement
    )
}