package com.healthtracking.app.composables.screens.eat

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.composables.ErrorMessageComponent
import com.healthtracking.app.composables.SaveAndCancelButtons
import com.healthtracking.app.composables.TextFieldWithErrorMessage
import com.healthtracking.app.entities.Food
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
        // Workout name input
        TextFieldWithErrorMessage(
            value = mealName,
            onValueChange = { viewModel.updateName(it) },
            labelId = R.string.meal_name_label,
            placeholderId = R.string.meal_name_placeholder,
            hasError = nameErrorMessageId != null,
            errorMessageId = nameErrorMessageId
        )

        // Add food button
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
        // Display exercises
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
                FoodCard(food = food, removeFood = { viewModel.removeFoodItem(food) })
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
fun FoodCard(
    food: Food,
    removeFood: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(60.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = food.name,
                style = MaterialTheme.typography.bodyMedium,
            )
            IconButton(onClick = { removeFood() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.delete),
                    contentDescription = stringResource(id = R.string.delete)
                )
            }
        }
    }
}