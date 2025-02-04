package com.healthtracking.app.composables.screens.eat

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Meal name input - later will have an option to select an already existing meal (but allow some tweaking of it)
        TextFieldWithErrorMessage(
            value = mealName,
            onValueChange = { viewModel.updateName(it) },
            labelId = R.string.meal_name_label,
            placeholderId = R.string.meal_name_placeholder,
            hasError = nameErrorMessageId != null,
            errorMessageId = nameErrorMessageId
        )

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDialog() {
    BasicAlertDialog(onDismissRequest = {}) {
        // food name (editable) - dropdown with options when the user types in something

        // display calculation of calories

        // protein (editable)

        // carbs (editable)

        // fats (editable)
    }
}