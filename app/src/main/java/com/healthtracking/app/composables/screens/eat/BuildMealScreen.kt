package com.healthtracking.app.composables.screens.eat

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.composables.HeaderAndListBox
import com.healthtracking.app.composables.SaveAndCancelButtons
import com.healthtracking.app.composables.TextFieldWithErrorMessage
import com.healthtracking.app.composables.screens.workout.FoodEntryDialog
import com.healthtracking.app.entities.Food
import com.healthtracking.app.viewmodels.screen.BuildMealViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun BuildMeal(
    modifier: Modifier,
    navController: NavController,
    viewModel: BuildMealViewModel,
) {
    val mealName = viewModel.name.collectAsStateWithLifecycle().value
    val nameErrorMessageId = viewModel.nameErrorMessageId.collectAsStateWithLifecycle().value

    val foodDialogOpen = rememberSaveable() { mutableStateOf(false) }
    FoodEntryDialog(
        isOpen = foodDialogOpen.value,
        onDismissRequest = { foodDialogOpen.value = false; viewModel.clearFoodDialog() },
        onSave = {
            if (viewModel.validFoodDialog()) {
                viewModel.addFoodItem()
                foodDialogOpen.value = false
            }
        },
        name = viewModel.dialogFoodName.collectAsStateWithLifecycle().value,
        measurement = viewModel.dialogMeasurement.collectAsStateWithLifecycle().value,
        protein = viewModel.dialogProtein.collectAsStateWithLifecycle().value,
        carbs = viewModel.dialogCarbs.collectAsStateWithLifecycle().value,
        fats = viewModel.dialogFats.collectAsStateWithLifecycle().value,
        quantity = viewModel.dialogQuantity.collectAsStateWithLifecycle().value,
        calories = viewModel.dialogCalories,
        updateName = { viewModel.updateDialogFoodName(it) },
        updateMeasurement = { viewModel.updateDialogMeasurement(it) },
        updateProtein = { viewModel.updateDialogProtein(it) },
        updateCarbs = { viewModel.updateDialogCarbs(it) },
        updateFats = { viewModel.updateDialogFats(it) },
        updateQuantity = { viewModel.updateDialogQuantity(it) },
        measurementOptions = BuildMealViewModel.MEASUREMENT_OPTIONS,
        nameHasError = viewModel.dialogNameHasError.collectAsStateWithLifecycle().value
    )

    val mealSearchDialogOpen = rememberSaveable() { mutableStateOf(false) }
    MealSearchDialog(
        isOpen = mealSearchDialogOpen.value,
        onDismissRequest = { mealSearchDialogOpen.value = false },
        mealSearch = viewModel.mealSearch.collectAsStateWithLifecycle().value ?: "",
        updateMealSearch = { viewModel.updateMealSearch(it) },
        mealList = viewModel.mealsFilteredBySearch().collectAsStateWithLifecycle().value,
        onMealSelected = { viewModel.reuseMealInfo(it); mealSearchDialogOpen.value = false }
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
            MealSearcher(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .fillMaxHeight()
                    .padding(top = 8.dp, bottom = 15.dp),
                openMealSearchDialog = { mealSearchDialogOpen.value = true }
            )

            TextFieldWithErrorMessage(
                modifier = Modifier.fillMaxWidth(1f),
                value = mealName,
                onValueChange = { viewModel.updateName(it) },
                label = stringResource(id = R.string.meal_name_label),
                placeholder = stringResource(id = R.string.meal_name_placeholder),
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
                    foodItemsFlow = viewModel.foodItems,
                    editFoodItem = {
                        viewModel.populateDialogWithFood(it)
                        foodDialogOpen.value = true
                    },
                    removeFoodItem = { viewModel.removeFoodItem(it) }
                )
            },
            isContentEmpty = viewModel.foodItems.collectAsStateWithLifecycle().value.isEmpty(),
            contentPlaceholderText = stringResource(id = R.string.no_existing_food_entries)
        )

        // Save and cancel buttons
        SaveAndCancelButtons(
            onSave = {
                if (viewModel.validMeal()) {
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
    foodItemsFlow: StateFlow<List<Food>>,
    editFoodItem: (Food) -> Unit,
    removeFoodItem: (Food) -> Unit,
) {
    val foodItems by foodItemsFlow.collectAsState(emptyList())

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        itemsIndexed (foodItems) { _, food ->
            NutritionCard(
                title = food.name,
                calories = food.calories.toDouble(),
                protein = food.protein.toDouble(),
                carbs = food.carbohydrates.toDouble(),
                fats = food.fats.toDouble(),
                onClick = { editFoodItem(food) },
                onDelete = { removeFoodItem(food) }
            )
        }
    }
}

@Composable
private fun MealSearcher(
    modifier: Modifier = Modifier,
    openMealSearchDialog: () -> Unit
) {
    IconButton(
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.tertiary),
        onClick = openMealSearchDialog
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(id = R.string.search_previous_meals)
        )
    }
}
