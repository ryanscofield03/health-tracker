package com.healthtracking.app.composables.screens.eat

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.healthtracking.app.R
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.Meal
import com.healthtracking.app.composables.ScreenHeader
import com.healthtracking.app.composables.TextFieldWithErrorMessage
import com.healthtracking.app.ui.theme.CaloriesColour
import com.healthtracking.app.ui.theme.CarbsColour
import com.healthtracking.app.ui.theme.FatsColour
import com.healthtracking.app.ui.theme.ProteinColour
import com.healthtracking.app.viewmodels.screen.MealScreenViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateDailyGoalDialog(
    onSubmit: () -> Boolean,
    onDismissRequest: () -> Unit,
    proteinGoal: String?,
    updateProteinGoal: (String?) -> Unit,
    validProteinGoal: Boolean,
    proteinErrorMessageId: Int,
    carbsGoal: String?,
    updateCarbsGoal: (String?) -> Unit,
    validCarbsGoal: Boolean,
    carbsErrorMessageId: Int,
    fatsGoal: String?,
    updateFatsGoal: (String?) -> Unit,
    validFatsGoal: Boolean,
    fatsErrorMessageId: Int
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.update_daily_food_goal),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Protein Goal Input Field
                TextFieldWithErrorMessage(
                    value = proteinGoal ?: "",
                    onValueChange = { updateProteinGoal(it) },
                    labelId = R.string.protein_goal_label,
                    placeholderId = R.string.protein_goal_label,
                    hasError = !validProteinGoal,
                    errorMessageId = proteinErrorMessageId
                )

                // Carbs Goal Input Field
                TextFieldWithErrorMessage(
                    value = carbsGoal ?: "",
                    onValueChange = { updateCarbsGoal(it) },
                    labelId = R.string.carbs_goal_label,
                    placeholderId = R.string.carbs_goal_placeholder,
                    hasError = !validCarbsGoal,
                    errorMessageId = carbsErrorMessageId
                )

                // Fats Goal Input Field
                TextFieldWithErrorMessage(
                    value = fatsGoal ?: "",
                    onValueChange = { updateFatsGoal(it) },
                    labelId = R.string.fats_goal_label,
                    placeholderId = R.string.fats_goal_placeholder,
                    hasError = !validFatsGoal,
                    errorMessageId = fatsErrorMessageId
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        if (onSubmit()) {
                            onDismissRequest()
                        }
                    }) {
                        Text(
                            text = stringResource(id = R.string.add),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    TextButton(onClick = { onDismissRequest() })
                    {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EatMain (
    modifier: Modifier,
    viewModel: MealScreenViewModel
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(headerStringId = R.string.eat_title)

        val openUpdateDailyGoalDialog = rememberSaveable { mutableStateOf(false) }
        if (openUpdateDailyGoalDialog.value) {
            UpdateDailyGoalDialog(
                onSubmit = { viewModel.updateGoals() },
                onDismissRequest = {
                    openUpdateDailyGoalDialog.value = false
                    viewModel.clearDialog()
                },
                proteinGoal = viewModel.dialogProteinValue,
                updateProteinGoal = { viewModel.updateDialogProtein(it) },
                validProteinGoal = viewModel.proteinDialogValueValid,
                proteinErrorMessageId = R.string.invalid_protein_goal,
                carbsGoal = viewModel.dialogCarbohydratesValue,
                updateCarbsGoal = { viewModel.updateDialogCarbohydrates(it) },
                validCarbsGoal = viewModel.carbsDialogValueValid,
                carbsErrorMessageId = R.string.invalid_carbs_goal,
                fatsGoal = viewModel.dialogFatsValue,
                updateFatsGoal = { viewModel.updateDialogFats(it) },
                validFatsGoal = viewModel.fatDialogValueValid,
                fatsErrorMessageId = R.string.invalid_fats_goal
            )
        }

        // Display today's calories and macros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            horizontalArrangement = Arrangement.spacedBy(12.dp))
        {
            Column(modifier = Modifier
                .fillMaxSize()
                .weight(0.65f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CaloriesCard(
                    onClick = { openUpdateDailyGoalDialog.value = true },
                    caloriesCurrent = 1800,
                    caloriesTotal = 2500
                )

                WeeklyGraph()
            }
            Box(modifier = Modifier.weight(0.35f)) {
                MacroCards(
                    onClick = { openUpdateDailyGoalDialog.value = true },
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
                Pair(
                    Meal(
                        name = "Breakfast Bagels",
                        date = LocalDateTime.now()
                    ),
                    listOf(
                        Food(name = "Bagel", calories = 250f, protein = 10f, carbohydrates = 49f, fat = 1.5f),
                    )
                ),
                Pair(
                    Meal(
                        name = "Lunch Bagels",
                        date = LocalDateTime.now()
                    ),
                    listOf(
                        Food(name = "Bagel", calories = 250f, protein = 10f, carbohydrates = 49f, fat = 1.5f),
                        Food(name = "Bagel", calories = 250f, protein = 10f, carbohydrates = 49f, fat = 1.5f)
                    )
                ),
                Pair(
                    Meal(
                        name = "Dinner Bagels",
                        date = LocalDateTime.now()
                    ),
                    listOf(
                        Food(name = "Bagel", calories = 250f, protein = 10f, carbohydrates = 49f, fat = 1.5f),
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
        )
    ) {
        Column(modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxSize()) {
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 4.dp),
                text = stringResource(id = R.string.weekly_calories_and_macros_graph_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )

            BarChart(
                caloriesProgressPercent = 60L,
                proteinProgressPercent = 30L,
                carbsProgressPercent = 20L,
                fatsProgressPercent = 10L
            )
        }
    }
}

@Composable
fun ColumnScope.CaloriesCard(
    caloriesCurrent: Long,
    caloriesTotal: Long,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.weight(0.31f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = onClick,
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
    onClick: () -> Unit,
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
            onClick = onClick,
            titleId = R.string.protein,
            progressString = "$proteinCurrent/${proteinTotal}g",
            progressFloat = (proteinCurrent.toFloat() / proteinTotal).coerceIn(0f, 1f),
            progressColour = ProteinColour
        )

        MacroCard(
            onClick = onClick,
            titleId = R.string.carbs,
            progressString = "$carbsCurrent/${carbsTotal}g",
            progressFloat = (carbsCurrent.toFloat() / carbsTotal).coerceIn(0f, 1f),
            progressColour = CarbsColour
        )

        MacroCard(
            onClick = onClick,
            titleId = R.string.fats,
            progressString = "$fatsCurrent/${fatsTotal}g",
            progressFloat = (fatsCurrent.toFloat() / fatsTotal).coerceIn(0f, 1f),
            progressColour = FatsColour
        )
    }
}

@Composable
fun ColumnScope.MacroCard(
    onClick: () -> Unit,
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
        onClick = onClick,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier
            .padding(6.dp)
            .fillMaxSize()
        ) {
            // display a bar filled to % of goal protein met
            Text(
                text = stringResource(id = titleId),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = progressString,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Box(modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.BottomEnd)){
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
    currentMealEntries: List<Pair<Meal, List<Food>>>
) {
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        itemsIndexed(currentMealEntries) { _, mealEntry ->
            MealEntryCard(mealEntry)
        }
    }
}

@Composable
fun MealEntryCard(
    mealEntry: Pair<Meal, List<Food>>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = mealEntry.first.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.5f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val formatter = DecimalFormat("0.#")
                val calories = formatter.format(mealEntry.second.sumOf { it.calories.toDouble() })
                val protein = formatter.format(mealEntry.second.sumOf { it.protein.toDouble() })
                val carbs = formatter.format(mealEntry.second.sumOf { it.carbohydrates.toDouble() })
                val fats = formatter.format(mealEntry.second.sumOf { it.fat.toDouble() })

                NutrientItem(
                    label = "K",
                    value = "${calories}kcal",
                    drawableId = R.drawable.calories,
                    iconColour = CaloriesColour
                )
                NutrientItem(
                    label = "P",
                    value = "${protein}g",
                    drawableId = R.drawable.protein,
                    iconColour = ProteinColour
                )
                NutrientItem(
                    label = "C",
                    value = "${carbs}g",
                    drawableId = R.drawable.carbs,
                    iconColour = CarbsColour
                )
                NutrientItem(
                    label = "F",
                    value = "${fats}g",
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
