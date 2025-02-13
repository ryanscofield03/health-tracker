package com.healthtracking.app.composables.screens.eat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.composables.CustomDialog
import com.healthtracking.app.composables.HeaderAndListBox
import com.healthtracking.app.composables.TextFieldWithErrorMessage
import com.healthtracking.app.entities.MealWithFoodList
import com.healthtracking.app.theme.CustomCutCornerShape

@Composable
internal fun MealSearchDialog(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    mealSearch: String,
    updateMealSearch: (String) -> Unit,
    mealList: List<MealWithFoodList>,
    onMealSelected: (MealWithFoodList) -> Unit
) {
    if (isOpen) {
        CustomDialog(
            modifier = Modifier.fillMaxHeight(0.7f),
            onDismissRequest = onDismissRequest
        ) {
            Column(modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)) {
                HeaderAndListBox(
                    modifier = Modifier.fillMaxHeight(0.87f),
                    header = {
                        TextFieldWithErrorMessage(
                            value = mealSearch,
                            onValueChange = { updateMealSearch(it) },
                            label = stringResource(id = R.string.search_past_meals_label),
                            placeholder = stringResource(id = R.string.search_past_meals_placeholder),
                            hasError = mealList.isEmpty() && mealSearch.isNotBlank(),
                            errorMessage = stringResource(R.string.no_meals_found_from_search, mealSearch.lowercase())
                        )
                    },
                    listContent = {
                        MealsList(
                            mealList = mealList,
                            onMealSelected = onMealSelected
                        )
                    },
                    isContentEmpty = mealList.isEmpty(),
                    contentPlaceholderText = stringResource(id = R.string.no_existing_meal_entries)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismissRequest
                    ) {
                        Text(
                            text = stringResource(id = R.string.close),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MealsList(
    mealList: List<MealWithFoodList>,
    onMealSelected: (MealWithFoodList) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        itemsIndexed(mealList) { _, mealWithFoodList ->
            MealWithFoodDropdownCard(
                mealWithFoodList = mealWithFoodList,
                onClick = { onMealSelected(mealWithFoodList) }
            )
        }
    }
}

@Composable
private fun MealWithFoodDropdownCard(
    mealWithFoodList: MealWithFoodList,
    onClick: () -> Unit
) {
    val expanded = rememberSaveable { mutableStateOf(false) }
    val rotation = animateFloatAsState(if (expanded.value) 180f else 0f, label = "")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        shape = CustomCutCornerShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mealWithFoodList.meal.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiary
                )

                IconButton(
                    onClick = { expanded.value = !expanded.value }
                ) {
                    Icon(
                        modifier = Modifier
                            .rotate(rotation.value)
                            .size(42.dp),
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = stringResource(id = R.string.expand_meal_card)
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = expanded.value,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
        ) + fadeIn(),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
        ) + fadeOut()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                mealWithFoodList.foodItems.forEachIndexed { index, foodItem ->
                    Text(
                        text = foodItem.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    // add horizontal divider between items
                    if (index < mealWithFoodList.foodItems.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.2f),
                            thickness = 0.8.dp
                        )
                    }
                }
            }
        }
    }
}
