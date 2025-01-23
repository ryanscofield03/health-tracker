package com.healthtracking.app.screens.eat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.healthtracking.app.R
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.Meal
import com.healthtracking.app.screens.ScreenHeader
import com.healthtracking.app.ui.theme.CaloriesColour
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

        // Display today's calories and macros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.18f),
            horizontalArrangement = Arrangement.spacedBy(12.dp))
        {
            CaloriesCard(
                weight = 0.3f,
                onClick = { },
                caloriesCurrent = 1800,
                caloriesTotal = 2500
            )
            MacroCards(
                weight = 0.7f,
                onClick = { },
                proteinCurrent = 80L,
                proteinTotal = 100L,
                carbsCurrent = 120L,
                carbsTotal = 200L,
                fatsCurrent = 15L,
                fatsTotal = 40L,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        
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
fun RowScope.CaloriesCard(
    weight: Float,
    onClick: () -> Unit,
    caloriesCurrent: Long,
    caloriesTotal: Long
) {
    Card(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        // display a bar filled to % of goal calories met
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 5.dp),
            text = stringResource(id = R.string.calories),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiary
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "$caloriesCurrent/$caloriesTotal",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiary
        )
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .height(6.dp),
                progress = { (caloriesCurrent.toFloat() / caloriesTotal).coerceIn(0f, 1f) },
                color = CaloriesColour,
                trackColor = Color.White,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun RowScope.MacroCards(
    weight: Float,
    onClick: () -> Unit,
    proteinCurrent: Long,
    proteinTotal: Long,
    carbsCurrent: Long,
    carbsTotal: Long,
    fatsCurrent: Long,
    fatsTotal: Long
) {
    Card(modifier = Modifier
        .weight(weight)
        .fillMaxHeight()
        .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceAround) {
            Card(modifier = Modifier.padding(8.dp)) {
                // display a bar filled to % of goal protein met
                Text(
                    text = stringResource(id = R.string.protein),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = "$proteinCurrent/$proteinTotal",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Box(modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterHorizontally)){
                    CircularProgressIndicator(
                        progress = {(proteinCurrent.toFloat() / proteinTotal).coerceIn(0f, 1f)},
                        color = ProteinColour,
                        trackColor = Color.White,
                        strokeWidth = 6.dp,
                    )
                }
            }

            Card(modifier = Modifier.padding(8.dp)) {
                // display a bar filled to % of goal carbs met
                Text(
                    text = stringResource(id = R.string.carbs),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = "$carbsCurrent/$carbsTotal",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Box(modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterHorizontally)){
                    CircularProgressIndicator(
                        progress = {(carbsCurrent.toFloat() / carbsTotal).coerceIn(0f, 1f)},
                        color = CarbsColour,
                        trackColor = Color.White,
                        strokeWidth = 6.dp,
                    )
                }
            }

            Card(modifier = Modifier.padding(8.dp)) {
                // display a bar filled to % of goal fat met
                Text(
                    text = stringResource(id = R.string.fats),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = "$fatsCurrent/$fatsTotal",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Column(
                    modifier = Modifier.size(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ){
                    CircularProgressIndicator(
                        progress = {(fatsCurrent.toFloat() / fatsTotal).coerceIn(0f, 1f)},
                        color = FatsColour,
                        trackColor = Color.White,
                        strokeWidth = 6.dp,
                    )
                }
            }
        }
    }
}

@Composable
fun AddMealEntry() {
    val mealEntryDialogOpen = rememberSaveable {mutableStateOf(false)}
    if (mealEntryDialogOpen.value) {
        AddMealEntryDialog()
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        onClick = { /*TODO open dialog */ }
    ) {
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
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = mealEntry.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onTertiary)

            mealEntry.foodItems.forEach { foodItem ->
                Box(modifier = Modifier
                    .padding(8.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.secondary.copy(0.4f))) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = foodItem.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Calories: ${foodItem.calories}kcal")
                        Text(text = "Protein: ${foodItem.protein}g")
                        Text(text = "Carbs: ${foodItem.carbohydrates}g")
                        Text(text = "Fat: ${foodItem.fat}g")
                    }
                }
            }
        }
    }
}
