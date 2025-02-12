package com.healthtracking.app.composables.screens.workout

import androidx.compose.runtime.Composable
import com.healthtracking.app.composables.CustomDialog
import com.healthtracking.app.entities.MealWithFoodList

@Composable
fun MealSearchDialog(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    mealList: List<MealWithFoodList>,
    onMealSelected: () -> Unit
) {
    CustomDialog(onDismissRequest = onDismissRequest) {

    }
}