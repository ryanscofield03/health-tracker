package com.healthtracking.app.screens.eat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.healthtracking.app.R
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.Meal
import com.healthtracking.app.screens.ScreenHeader
import com.healthtracking.app.ui.theme.CarbsColour
import com.healthtracking.app.ui.theme.FatsColour
import com.healthtracking.app.ui.theme.ProteinColour

@Composable
fun EatMain (
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            headerStringId = R.string.eat_title,
            spacerSize = 8.dp
        )

        // Update the daily goal of calories, protein, carbs, and fat
        UpdateDailyGoal()

        // Display today's calories and macros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp))
        {
            CaloriesCard(caloriesCurrent = 1800, caloriesTotal = 2500)
            MacrosCard(
                proteinCurrent = 80L,
                proteinTotal = 100L,
                carbsCurrent = 120L,
                carbsTotal = 200L,
                fatsCurrent = 15L,
                fatsTotal = 40L,
            )
        }

        // Allow for adding/editing/deleting food entries
        AddMealEntry()

        // Display current meal entries for this day
        CurrentMealEntryList(
            listOf(
                Meal(
                    name = "Breakfast Bagels",
                    foodItems = listOf(
                        Food(name = "Bagel", calories = 250f, protein = 10f, carbohydrates = 49f, fat = 1.5f),
                        Food(name = "Bagel", calories = 250f, protein = 10f, carbohydrates = 49f, fat = 1.5f)
                    )
                ),
                Meal(
                    name = "Dinner Bagels",
                    foodItems = listOf(
                        Food(name = "Bagel", calories = 250f, protein = 10f, carbohydrates = 49f, fat = 1.5f),
                        Food(name = "Bagel", calories = 250f, protein = 10f, carbohydrates = 49f, fat = 1.5f),
                        Food(name = "Bagel", calories = 250f, protein = 10f, carbohydrates = 49f, fat = 1.5f)
                    )
                )
            )
        )
    }
}

@Composable
fun UpdateDailyGoal() {
    val dailGoalDialogOpen = rememberSaveable {mutableStateOf(false)}
    if (dailGoalDialogOpen.value) {
        UpdateDailyGoalDialog()
    }

    Button(onClick = { /*TODO open dialog */ }) {
        Text(text = stringResource(id = R.string.update_daily_food_goal))
    }
}

@Composable
fun UpdateDailyGoalDialog() {
    Dialog(onDismissRequest = { /*TODO*/ }) {

    }
}

@Composable
fun CaloriesCard(
    caloriesCurrent: Long,
    caloriesTotal: Long
) {
    Card {
        // display a bar filled to % of goal calories met
        Text(text = stringResource(id = R.string.calories))
        Text(text = "$caloriesCurrent/$caloriesTotal")
        LinearProgressIndicator(progress = { (caloriesCurrent / caloriesTotal).toFloat() })
    }
}

@Composable
fun MacrosCard(
    proteinCurrent: Long,
    proteinTotal: Long,
    carbsCurrent: Long,
    carbsTotal: Long,
    fatsCurrent: Long,
    fatsTotal: Long,
) {
    Card {
        // display a bar filled to % of goal protein met
        Text(text = stringResource(id = R.string.protein))
        Text(text = "$proteinCurrent/$proteinTotal")
        CircularProgressIndicator(
            progress = { (proteinCurrent / proteinTotal).toFloat() },
            color = ProteinColour,
            strokeWidth = 8.dp
        )

        // display a bar filled to % of goal carbs met
        Text(text = stringResource(id = R.string.carbs))
        Text(text = "$carbsCurrent/$carbsTotal")
        CircularProgressIndicator(
            progress = { (carbsCurrent / carbsTotal).toFloat() },
            color = CarbsColour,
            strokeWidth = 8.dp
        )

        // display a bar filled to % of goal fat met
        Text(text = stringResource(id = R.string.fats))
        Text(text = "$fatsCurrent/$fatsTotal")
        CircularProgressIndicator(
            progress = { (fatsCurrent / fatsTotal).toFloat() },
            color = FatsColour,
            strokeWidth = 8.dp
        )
    }
}

@Composable
fun AddMealEntry() {
    val mealEntryDialogOpen = rememberSaveable {mutableStateOf(false)}
    if (mealEntryDialogOpen.value) {
        AddMealEntryDialog()
    }

    Button(onClick = { /*TODO open dialog */ }) {
        Text(text = stringResource(id = R.string.add_meal_entry))
    }
}

@Composable
fun AddMealEntryDialog() {
    Dialog(onDismissRequest = { /*TODO*/ }) {

    }
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
