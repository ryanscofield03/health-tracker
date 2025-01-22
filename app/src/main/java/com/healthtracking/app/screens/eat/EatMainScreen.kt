package com.healthtracking.app.screens.eat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.Meal
import com.healthtracking.app.screens.ScreenHeader

@Composable
fun EatMain (
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            headerStringId = R.string.eat_title,
            spacerSize = 16.dp
        )

        // Update the daily goal of calories, protein, carbs, and fat
        UpdateDailyGoal()

        // Display today's calories and macros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp))
        {
            CaloriesCard()
            MacrosCard()
        }

        // Allow for adding/editing/deleting food entries
        AddMealEntry()

        // Display current meal entries for this day
        CurrentMealEntryList(
            listOf(
                Meal(
                    name = "Breakfast Bagel",
                    foodItems = listOf(
                        Food(name = "Bagel", calories = 250f, protein = 10f, carbohydrates = 49f, fat = 1.5f)
                    )
                )
            )
        )
    }
}

@Composable
fun UpdateDailyGoal() {
    TODO("Not yet implemented")
}

@Composable
fun CaloriesCard() {
    TODO("Not yet implemented")
}

@Composable
fun MacrosCard() {
    TODO("Not yet implemented")
}

@Composable
fun AddMealEntry() {
    TODO("Not yet implemented")
}

@Composable
fun CurrentMealEntryList(
    currentMealEntries: List<Meal>
) {
    LazyColumn {
        itemsIndexed(currentMealEntries) { _, mealEntry ->
            MealEntryCard(
                mealEntry
            )
        }
    }
}

@Composable
fun MealEntryCard(
    mealEntry: Meal
) {
    Card {
        Text(text = mealEntry.name)

        mealEntry.foodItems.forEach { foodItem ->
            Text(text = foodItem.name)
            Text(text = "Calories: ${foodItem.calories}kcal")
            Text(text = "Protein: ${foodItem.protein}g")
            Text(text = "Carbs: ${foodItem.carbohydrates}g")
            Text(text = "Fat: ${foodItem.fat}g")
        }
    }
}
