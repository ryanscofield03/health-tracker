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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.healthtracking.app.composables.SaveAndCancelButtons
import com.healthtracking.app.composables.TextFieldWithErrorMessage
import com.healthtracking.app.viewmodels.screen.BuildMealViewModel

@Composable
fun BuildMeal(
    modifier: Modifier,
    navController: NavController,
    viewModel: BuildMealViewModel,
) {
    val mealName = viewModel.name.collectAsState().value
    val nameErrorMessageId = viewModel.nameErrorMessageId.collectAsState().value
    val foodItemsErrorMessageId = viewModel.foodItemsErrorMessageId.collectAsState().value

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
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
        Spacer(modifier = Modifier.height(16.dp))
        // Add food item button
        Button(
            onClick = { /** TODO open food dialog */},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(stringResource(id = R.string.add_food_button))
        }
        Spacer(modifier = Modifier.height(7.dp))
        // Display food items in cards
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .border(
                width = 1.dp,
                color =
                if (foodItemsErrorMessageId != null) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            itemsIndexed (viewModel.foodItems.value) { _, food ->
                NutritionCard(
                    modifier = modifier.clickable { viewModel.removeFoodItem(food) },
                    title = food.name,
                    calories = food.calories.toDouble(),
                    protein = food.protein.toDouble(),
                    carbs = food.carbohydrates.toDouble(),
                    fats = food.fats.toDouble()
                )
            }
        }
        ErrorMessageComponent(
            hasError = foodItemsErrorMessageId != null,
            errorMessageId = foodItemsErrorMessageId
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
private fun FoodDialog() {
    BasicAlertDialog(onDismissRequest = {}) {
        // food name (editable) - dropdown with options when the user types in something

        // display calculation of calories

        // protein (editable)

        // carbs (editable)

        // fats (editable)
    }
}