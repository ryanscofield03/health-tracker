package com.healthtracking.app.screens.eat

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
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
        ScreenHeader(headerStringId = R.string.eat_title)

        // Display today's calories and macros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            horizontalArrangement = Arrangement.spacedBy(12.dp))
        {
            Column(modifier = Modifier.weight(0.7f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CaloriesCard(
                    caloriesCurrent = 1800,
                    caloriesTotal = 2500
                )

                WeeklyGraph()
            }
            Box(modifier = Modifier.weight(0.3f)) {
                MacroCards(
                    proteinCurrent = 80L,
                    proteinTotal = 100L,
                    carbsCurrent = 120L,
                    carbsTotal = 200L,
                    fatsCurrent = 15L,
                    fatsTotal = 40L,
                )
            }
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
fun ColumnScope.WeeklyGraph() {
    Card(
        modifier = Modifier
            .weight(2 / 3f)
            .fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(text = "TEST")
    }
}

@Composable
fun ColumnScope.CaloriesCard(
    caloriesCurrent: Long,
    caloriesTotal: Long
) {
    Card(
        modifier = Modifier.weight(0.31f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = CardDefaults.cardElevation(4.dp)
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
            text = "$caloriesCurrent/${caloriesTotal}kcal",
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
fun MacroCards(
    proteinCurrent: Long,
    proteinTotal: Long,
    carbsCurrent: Long,
    carbsTotal: Long,
    fatsCurrent: Long,
    fatsTotal: Long
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MacroCard(
            titleId = R.string.protein,
            progressString = "$proteinCurrent/${proteinTotal}g",
            progressFloat = (proteinCurrent.toFloat() / proteinTotal).coerceIn(0f, 1f),
            progressColour = ProteinColour
        )

        MacroCard(
            titleId = R.string.carbs,
            progressString = "$carbsCurrent/${carbsTotal}g",
            progressFloat = (carbsCurrent.toFloat() / carbsTotal).coerceIn(0f, 1f),
            progressColour = CarbsColour
        )

        MacroCard(
            titleId = R.string.fats,
            progressString = "$fatsCurrent/${fatsTotal}g",
            progressFloat = (fatsCurrent.toFloat() / fatsTotal).coerceIn(0f, 1f),
            progressColour = FatsColour
        )
    }
}

@Composable
fun ColumnScope.MacroCard(
    titleId: Int,
    progressString: String,
    progressFloat: Float,
    progressColour: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier
            .padding(6.dp)
            .fillMaxSize()) {
            // display a bar filled to % of goal protein met
            Text(
                text = stringResource(id = titleId),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = progressString,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Box(modifier = Modifier
                .size(32.dp)
                .align(Alignment.End)){
                CircularProgressIndicator(
                    progress = { progressFloat },
                    color = progressColour,
                    trackColor = Color.White,
                    strokeWidth = 6.dp,
                )
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
        shape = RoundedCornerShape(8.dp),
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = mealEntry.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.5f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val formatter = DecimalFormat("0.#")
                NutrientItem(
                    label = "P",
                    value = formatter.format(mealEntry.foodItems.sumOf { it.protein.toDouble() }),
                    drawableId = R.drawable.protein,
                    iconColour = ProteinColour
                )
                NutrientItem(
                    label = "C",
                    value = formatter.format(mealEntry.foodItems.sumOf { it.carbohydrates.toDouble() }),
                    drawableId = R.drawable.carbs,
                    iconColour = CarbsColour
                )
                NutrientItem(
                    label = "F",
                    value = formatter.format(mealEntry.foodItems.sumOf { it.fat.toDouble() }),
                    drawableId = R.drawable.fats,
                    iconColour = FatsColour
                )
            }
        }
    }
}

@Composable
fun NutrientItem(label: String, value: String, drawableId: Int, iconColour: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = label,
            tint = iconColour,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}
