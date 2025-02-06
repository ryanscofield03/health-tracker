package com.healthtracking.app.composables.screens.eat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.composables.ErrorMessageComponent
import com.healthtracking.app.composables.HeaderAndListBox
import com.healthtracking.app.composables.SaveAndCancelButtons
import com.healthtracking.app.composables.SelectionDropDown
import com.healthtracking.app.composables.SliderWithLabel
import com.healthtracking.app.composables.TextFieldWithErrorMessage
import com.healthtracking.app.entities.Food
import com.healthtracking.app.theme.CarbsColour
import com.healthtracking.app.theme.FatsColour
import com.healthtracking.app.theme.ProteinColour
import com.healthtracking.app.viewmodels.screen.BuildMealViewModel
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

@Composable
fun BuildMeal(
    modifier: Modifier,
    navController: NavController,
    viewModel: BuildMealViewModel,
) {
    val mealName = viewModel.name.collectAsState().value
    val nameErrorMessageId = viewModel.nameErrorMessageId.collectAsState().value
    val foodItemsErrorMessageId = viewModel.foodItemsErrorMessageId.collectAsState().value

    val foodDialogOpen = rememberSaveable() { mutableStateOf(false) }
    FoodDialog(
        isOpen = foodDialogOpen.value,
        onDismissRequest = { foodDialogOpen.value = false },
        onSave = {
            foodDialogOpen.value = false
            viewModel.addFoodItem(it)
        },
        name = viewModel.dialogFoodName.collectAsState().value,
        measurement = viewModel.dialogMeasurement.collectAsState().value,
        protein = viewModel.dialogProtein.collectAsState().value,
        carbs = viewModel.dialogCarbs.collectAsState().value,
        fats = viewModel.dialogFats.collectAsState().value,
        quantity = viewModel.dialogQuantity.collectAsState().value,
        calories = viewModel.dialogCalories,
        updateName = { viewModel.updateDialogFoodName(it) },
        updateMeasurement = { viewModel.updateDialogMeasurement(it) },
        updateProtein = { viewModel.updateDialogProtein(it) },
        updateCarbs = { viewModel.updateDialogCarbs(it) },
        updateFats = { viewModel.updateDialogFats(it) },
        updateQuantity = { viewModel.updateDialogQuantity(it) },
        measurementOptions = BuildMealViewModel.MEASUREMENT_OPTIONS
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placed on LHS so the user doesn't think they are searching when they type in a meal name
            // (maybe a different icon would be better)
            MealSearcher(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .fillMaxHeight()
                    .padding(top = 8.dp, bottom = 15.dp)
            )

            TextFieldWithErrorMessage(
                modifier = Modifier.fillMaxWidth(1f),
                value = mealName,
                onValueChange = { viewModel.updateName(it) },
                labelId = R.string.meal_name_label,
                placeholderId = R.string.meal_name_placeholder,
                hasError = nameErrorMessageId != null,
                errorMessageId = nameErrorMessageId
            )

        }
        Spacer(modifier = Modifier.height(7.dp))
        // Display food items in cards
        HeaderAndListBox(
            modifier = Modifier.fillMaxHeight(0.85f),
            header = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.05f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.food_items_title),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiary
                    )

                    IconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = { foodDialogOpen.value = true },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.add_food_button)
                        )
                    }
                }
            },
            listContent = {
                FoodItemsList(
                    modifier = modifier,
                    foodItemsFlow = viewModel.foodItems,
                    removeFoodItem = { viewModel.removeFoodItem(it) }
                )
            },
            isContentEmpty = false, // TODO
            contentPlaceholderText = "FIX ME LATER" // TODO
        )

        // Save and cancel buttons
        SaveAndCancelButtons(
            onSave = {
                if (viewModel.validateMeal()) {
                    navController.navigate("Eat")
                    viewModel.save()
                }
            },
            onCancel = {
                navController.navigate("Eat")
                viewModel.clear()
            }
        )
    }
}

@Composable
private fun FoodItemsList(
    modifier: Modifier = Modifier,
    foodItemsFlow: StateFlow<List<Food>>,
    removeFoodItem: (Food) -> Unit,
) {
    LazyColumn() {
        itemsIndexed (foodItemsFlow.value) { _, food ->
            NutritionCard(
                modifier = modifier.clickable { removeFoodItem(food) },
                title = food.name,
                calories = food.calories.toDouble(),
                protein = food.protein.toDouble(),
                carbs = food.carbohydrates.toDouble(),
                fats = food.fats.toDouble()
            )
        }
    }
}

@Composable
private fun MealSearcher(
    modifier: Modifier = Modifier
) {
    val mealSearcherDialogOpen = rememberSaveable() { mutableStateOf( false) }

    IconButton(
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.tertiary),
        onClick = { mealSearcherDialogOpen.value = true }
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(id = R.string.search_previous_meals)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onSave: (Food) -> Unit,
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
    measurementOptions: List<String>
) {
    if (isOpen) {
        BasicAlertDialog(onDismissRequest = {}) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
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
                        hasError = false,
                        errorMessageId = null
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
                        text = "Total calories: ${String.format(Locale.US, "%.0f", calories)}kcal",
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
                        maxSliderValue = 100f
                    )

                    // carbs slider
                    SliderWithLabel(
                        label = stringResource(id = R.string.carbs_formatted, String.format(Locale.US, "%.1f", carbs)),
                        value = carbs,
                        color = CarbsColour,
                        onValueChange = updateCarbs,
                        maxSliderValue = 100f
                    )

                    // fats slider
                    SliderWithLabel(
                        label = stringResource(id = R.string.fats_formatted, String.format(Locale.US, "%.1f", fats)),
                        value = fats,
                        color = FatsColour,
                        onValueChange = updateFats,
                        maxSliderValue = 100f
                    )

                    // quantity slider
                    SliderWithLabel(
                        label = stringResource(id = R.string.quantity_formatted, String.format(Locale.US, "%.0f", quantity)),
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
                        TextButton(onClick = {}) {
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
}

@Composable
fun MeasurementDropdown(
    measurement: String,
    measurementOptions: List<String>,
    updateMeasurement: (String) -> Unit
) {
    SelectionDropDown(
        items = measurementOptions,
        selectedText = measurement,
        onItemClick = updateMeasurement
    )
}
