package com.healthtracking.app.composables.screens.eat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.CustomDialog
import com.healthtracking.app.composables.SelectionDropDown
import com.healthtracking.app.composables.SliderWithLabel
import com.healthtracking.app.composables.TextFieldWithErrorMessage
import com.healthtracking.app.services.toDecimalPoints
import com.healthtracking.app.services.toStringWithDecimalPoints
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
    isLoadingNutrientData: Boolean,
    loadNutrientData: () -> Unit,
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
                    label = stringResource(id = R.string.food_name_label),
                    placeholder = stringResource(id = R.string.food_name_placeholder),
                    hasError = nameHasError,
                    errorMessage = stringResource(id = R.string.food_name_error_message)
                )

                // measurement dropdown
                SelectionDropDown(
                    label = stringResource(id = R.string.select_measurement_settings),
                    items = measurementOptions,
                    selectedText = measurement,
                    onItemClick = updateMeasurement
                )

                Spacer(modifier = Modifier.height(8.dp))

                NutrientsBlock(
                    modifier = Modifier,
                    calories = calories,
                    protein = protein,
                    carbs = carbs,
                    fats = fats,
                    quantity = quantity,
                    isLoadingNutrientData = isLoadingNutrientData,
                    canLoadNutrientData = !isLoadingNutrientData && !nameHasError,
                    loadNutrientData = loadNutrientData,
                    updateProtein = updateProtein,
                    updateCarbs = updateCarbs,
                    updateFats = updateFats,
                    updateQuantity = updateQuantity
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
                            text = stringResource(id = R.string.save),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    TextButton(onClick = {
                        onDismissRequest()
                    }) {
                        Text(
                            text = stringResource(id = R.string.cancel),
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
private fun NutrientsBlock(
    modifier: Modifier,
    protein: Float,
    carbs: Float,
    fats: Float,
    quantity: Float,
    calories: Float,
    isLoadingNutrientData: Boolean,
    canLoadNutrientData: Boolean,
    loadNutrientData: () -> Unit,
    updateProtein: (Float) -> Unit,
    updateCarbs: (Float) -> Unit,
    updateFats: (Float) -> Unit,
    updateQuantity: (Float) -> Unit
) {
    Box(modifier = Modifier.fillMaxHeight(0.7f)) {
        // Loading overlay
        if (isLoadingNutrientData) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.5f))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            // display calculation of calories
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(id = R.string.total_calories_formatted,
                        String.format(Locale.US, "%.0f", calories.times(quantity))),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiary
                )

                IconButton(onClick = loadNutrientData, enabled = canLoadNutrientData) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(id = R.string.refresh_nutrient_data)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // protein slider
            SliderWithLabel(
                label = stringResource(id = R.string.protein_formatted,
                    protein.toDecimalPoints(1).toStringWithDecimalPoints()),
                value = protein,
                color = ProteinColour,
                onValueChange = updateProtein,
                maxSliderValue = 100f,
                incrementAmount = 0.2f,
                enabled = !isLoadingNutrientData
            )

            // carbs slider
            SliderWithLabel(
                label = stringResource(id = R.string.carbs_formatted,
                    carbs.toDecimalPoints(1).toStringWithDecimalPoints()),
                value = carbs,
                color = CarbsColour,
                onValueChange = updateCarbs,
                maxSliderValue = 100f,
                incrementAmount = 0.2f,
                enabled = !isLoadingNutrientData
            )

            // fats slider
            SliderWithLabel(
                label = stringResource(id = R.string.fats_formatted,
                    fats.toDecimalPoints(1).toStringWithDecimalPoints()),
                value = fats,
                color = FatsColour,
                onValueChange = updateFats,
                maxSliderValue = 100f,
                incrementAmount = 0.2f,
                enabled = !isLoadingNutrientData
            )

            // quantity slider
            SliderWithLabel(
                label = stringResource(id = R.string.quantity_formatted,
                    quantity.toDecimalPoints(0).toStringWithDecimalPoints()),
                value = quantity,
                color = MaterialTheme.colorScheme.surfaceVariant,
                onValueChange = updateQuantity,
                maxSliderValue = 10f,
                enabled = !isLoadingNutrientData
            )
        }
    }
}
