package com.healthtracking.app.composables.screens.eat

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.healthtracking.app.R
import com.healthtracking.app.composables.BackgroundBorderBox
import com.healthtracking.app.composables.HeaderAndListBox
import com.healthtracking.app.composables.graphs.eat.BarChart
import com.healthtracking.app.entities.MealWithFoodList
import com.healthtracking.app.services.toDecimalPoints
import com.healthtracking.app.services.toStringWithDecimalPoints
import com.healthtracking.app.theme.CaloriesColour
import com.healthtracking.app.theme.CarbsColour
import com.healthtracking.app.theme.CustomCutCornerShape
import com.healthtracking.app.theme.FatsColour
import com.healthtracking.app.theme.ProteinColour
import com.healthtracking.app.viewmodels.screen.FoodViewModel

@Composable
fun EatMain (
    modifier: Modifier,
    navController: NavController,
    viewModel: FoodViewModel
) {
    Column(modifier = modifier.fillMaxSize()) {
        val openUpdateDailyGoalDialog = rememberSaveable { mutableStateOf(false) }
        MacroGoalDialog(
            isOpen = openUpdateDailyGoalDialog.value,
            onSubmit = { viewModel.updateGoals() },
            onDismissRequest = {
                openUpdateDailyGoalDialog.value = false
                viewModel.clearDialog()
            },
            caloriesGoal = viewModel.dialogCaloriesGoal,
            proteinGoal = viewModel.dialogProteinValue,
            updateProteinGoal = { viewModel.updateDialogProtein(it) },
            carbsGoal = viewModel.dialogCarbohydratesValue,
            updateCarbsGoal = { viewModel.updateDialogCarbohydrates(it) },
            fatsGoal = viewModel.dialogFatsValue,
            updateFatsGoal = { viewModel.updateDialogFats(it) },
            maxProtein = viewModel.getMaxProtein(),
            maxCarbs = viewModel.getMaxCarbs(),
            maxFats = viewModel.getMaxFats()
        )

        // Display today's calories and macros
        BackgroundBorderBox {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(id = R.string.dashboard_title),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp))
                {
                    val openEditGoalDialog = {
                        openUpdateDailyGoalDialog.value = true
                        viewModel.populateDialogEntries()
                    }

                    Column(modifier = Modifier
                        .fillMaxSize()
                        .weight(0.55f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CaloriesCard(
                            onClick = openEditGoalDialog,
                            caloriesCurrent = viewModel.currentCalories
                                .collectAsStateWithLifecycle(0f).value,
                            caloriesTotal = viewModel.goalCalories.collectAsStateWithLifecycle().value
                        )

                        WeeklyGraph(
                            caloriesProgressPercent = viewModel.weeklyCaloriesPercent
                                .collectAsStateWithLifecycle(0f).value,
                            proteinProgressPercent = viewModel.weeklyProteinPercent
                                .collectAsStateWithLifecycle(0f).value,
                            carbsProgressPercent = viewModel.weeklyProteinPercent
                                .collectAsStateWithLifecycle(0f).value,
                            fatsProgressPercent = viewModel.weeklyFatsPercent
                                .collectAsStateWithLifecycle(0f).value
                        )
                    }
                    Box(modifier = Modifier.weight(0.35f)) {
                        MacroCards(
                            onClick = openEditGoalDialog,
                            proteinCurrent = viewModel.currentProtein
                                .collectAsStateWithLifecycle(0f).value,
                            proteinTotal = viewModel.goalProtein
                                .collectAsStateWithLifecycle().value,
                            carbsCurrent = viewModel.currentCarbohydrates
                                .collectAsStateWithLifecycle(0f).value,
                            carbsTotal = viewModel.goalCarbohydrates
                                .collectAsStateWithLifecycle().value,
                            fatsCurrent = viewModel.currentFats
                                .collectAsStateWithLifecycle(0f).value,
                            fatsTotal = viewModel.goalFats
                                .collectAsStateWithLifecycle().value,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxSize()) {
            HeaderAndListBox(
                header = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.todays_entries_title),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                        // Allow for adding/editing/deleting food entries
                        AddMealEntry(addMealNavigation = { navController.navigate("AddMeal") })
                    }
                },
                listContent = {
                    CurrentMealEntryList(
                        modifier = Modifier.fillMaxHeight(1f),
                        onCardClick = { id: Long -> navController.navigate("EditMeal/$id") },
                        currentMealEntries = viewModel.currentMealEntries.collectAsState(listOf()).value ?: listOf(),
                        onCardDelete = { viewModel.deleteMeal(it) }
                    )
                },
                isContentEmpty = viewModel.currentMealEntries.collectAsState(listOf()).value?.isEmpty() ?: false,
                contentPlaceholderText = stringResource(id = R.string.no_existing_meal_entries_today)
            )
        }
    }
}

@Composable
private fun ColumnScope.WeeklyGraph(
    caloriesProgressPercent: Float,
    proteinProgressPercent: Float,
    carbsProgressPercent: Float,
    fatsProgressPercent: Float
) {
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
            .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 4.dp),
                text = stringResource(id = R.string.weekly_calories_and_macros_graph_title),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )

            BarChart(
                caloriesProgressPercent = caloriesProgressPercent,
                proteinProgressPercent = proteinProgressPercent,
                carbsProgressPercent = carbsProgressPercent,
                fatsProgressPercent = fatsProgressPercent
            )
        }
    }
}

@Composable
private fun ColumnScope.CaloriesCard(
    caloriesCurrent: Float,
    caloriesTotal: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.weight(0.31f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        shape = CustomCutCornerShape,
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
            text = "${caloriesCurrent.toStringWithDecimalPoints()}/${caloriesTotal}kcal",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiary
        )
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .height(6.dp),
                progress = { (caloriesCurrent / caloriesTotal).coerceIn(0f, 1f) },
                color = CaloriesColour,
                trackColor = Color.White,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun MacroCards(
    onClick: () -> Unit,
    proteinCurrent: Float,
    proteinTotal: Int,
    carbsCurrent: Float,
    carbsTotal: Int,
    fatsCurrent: Float,
    fatsTotal: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MacroCard(
            onClick = onClick,
            titleId = R.string.protein,
            progressString = "${proteinCurrent.toDecimalPoints(1).toStringWithDecimalPoints()}/${proteinTotal}g",
            progressFloat = (proteinCurrent / proteinTotal).coerceIn(0f, 1f),
            progressColour = ProteinColour
        )

        MacroCard(
            onClick = onClick,
            titleId = R.string.carbs,
            progressString = "${carbsCurrent.toDecimalPoints(1).toStringWithDecimalPoints()}/${carbsTotal}g",
            progressFloat = (carbsCurrent / carbsTotal).coerceIn(0f, 1f),
            progressColour = CarbsColour
        )

        MacroCard(
            onClick = onClick,
            titleId = R.string.fats,
            progressString = "${fatsCurrent.toDecimalPoints(1).toStringWithDecimalPoints()}/${fatsTotal}g",
            progressFloat = (fatsCurrent / fatsTotal).coerceIn(0f, 1f),
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
        shape = CustomCutCornerShape,
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
fun AddMealEntry(addMealNavigation: () -> Unit) {
    IconButton(
        modifier = Modifier.size(20.dp),
        onClick = addMealNavigation
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.add_meal_entry))
    }
}

@Composable
fun CurrentMealEntryList(
    modifier: Modifier = Modifier,
    onCardClick: (Long) -> Unit,
    onCardDelete: (Long) -> Unit,
    currentMealEntries: List<MealWithFoodList>,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(currentMealEntries) { _, mealEntry ->
            val mealId = mealEntry.meal.id

            NutritionCard(
                title = mealEntry.meal.name,
                calories = mealEntry.foodItems.sumOf { it.calories.toDouble() },
                protein = mealEntry.foodItems.sumOf { it.protein.toDouble() },
                carbs = mealEntry.foodItems.sumOf { it.carbohydrates.toDouble() },
                fats = mealEntry.foodItems.sumOf { it.fats.toDouble() },
                onClick = { onCardClick(mealId) },
                onDelete = { onCardDelete(mealId) }
            )
        }
    }
}
