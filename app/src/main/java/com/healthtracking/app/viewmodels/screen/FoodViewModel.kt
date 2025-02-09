package com.healthtracking.app.viewmodels.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.MealWithFoodList
import com.healthtracking.app.viewmodels.database.MealViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class FoodViewModel(private val mealViewModel: MealViewModel): ViewModel() {
    val currentCalories: Flow<Float> get() = calculateCurrentCalories()
    val currentProtein: Flow<Float> get() = calculateCurrentProtein()
    val currentCarbohydrates: Flow<Float> get() = calculateCurrentCarbohydrates()
    val currentFats: Flow<Float> get() = calculateCurrentFats()

    val goalCalories get() = mealViewModel.goalCalories
    val goalProtein get() = mealViewModel.goalProtein
    val goalCarbohydrates get() = mealViewModel.goalCarbohydrates
    val goalFats get() = mealViewModel.goalFats

    val dialogCaloriesGoal: String get() = dialogProteinValue.toInt().times(4)
        .plus(dialogCarbohydratesValue.toInt().times(4))
        .plus(dialogFatsValue.toInt().times(9)).toString()

    private val _dialogProteinValue: MutableState<Float> = mutableFloatStateOf(goalProtein.toFloat())
    val dialogProteinValue: Float get() = _dialogProteinValue.value

    private val _dialogCarbohydratesValue: MutableState<Float> = mutableFloatStateOf(goalCarbohydrates.toFloat())
    val dialogCarbohydratesValue: Float get() = _dialogCarbohydratesValue.value

    private val _dialogFatsValue: MutableState<Float> = mutableFloatStateOf(goalFats.toFloat())
    val dialogFatsValue: Float get() = _dialogFatsValue.value

    private val _currentMealEntries: MutableState<Flow<List<MealWithFoodList>?>> = mutableStateOf(flow{listOf<MealWithFoodList>()})
    val currentMealEntries: Flow<List<MealWithFoodList>?> get() = _currentMealEntries.value

    init {
        viewModelScope.launch {
            _currentMealEntries.value = mealViewModel.getTodaysMealEntries()
        }
    }

    /**
     * Deletes the meal from the database where id = mealId
     */
    fun deleteMeal(mealId: Long) {
        viewModelScope.launch {
            mealViewModel.deleteMeal(mealId)
        }
    }

    /**
     * Calculate the current calories consumed by the user on this day
     */
    private fun calculateCurrentCalories(): Flow<Float> {
        return currentMealEntries.map { mealList: List<MealWithFoodList>? ->
            mealList?.map { mealWithFood: MealWithFoodList ->
                mealWithFood.foodItems.map { foodItem: Food ->
                    foodItem.calories.times(foodItem.quantity)
                }
            }?.flatten()?.sum() ?: 0f
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 1)
    }

    /**
     * Calculate the current protein consumed by the user on this day
     */
    private fun calculateCurrentProtein(): Flow<Float> {
        return currentMealEntries.map { mealList: List<MealWithFoodList>? ->
            mealList?.map { mealWithFood: MealWithFoodList ->
                mealWithFood.foodItems.map { foodItem: Food ->
                    foodItem.protein.times(foodItem.quantity)
                }
            }?.flatten()?.sum() ?: 0f
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 1)
    }

    /**
     * Calculate the current carbs consumed by the user on this day
     */
    private fun calculateCurrentCarbohydrates(): Flow<Float> {
        return currentMealEntries.map { mealList: List<MealWithFoodList>? ->
            mealList?.map { mealWithFood: MealWithFoodList ->
                mealWithFood.foodItems.map { foodItem: Food ->
                    foodItem.carbohydrates.times(foodItem.quantity)
                }
            }?.flatten()?.sum() ?: 0f
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 1)
    }

    /**
     * Calculate the current fats consumed by the user on this day
     */
    private fun calculateCurrentFats(): Flow<Float> {
        return currentMealEntries.map { mealList: List<MealWithFoodList>? ->
            mealList?.map { mealWithFood: MealWithFoodList ->
                mealWithFood.foodItems.map { foodItem: Food ->
                    foodItem.fats.times(foodItem.quantity)
                }
            }?.flatten()?.sum() ?: 0f
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 1)
    }

    /**
     * Populate the dialog box with the current goals
     */
    fun populateDialogEntries() {
        _dialogProteinValue.value = goalProtein.toFloat()
        _dialogCarbohydratesValue.value = goalCarbohydrates.toFloat()
        _dialogFatsValue.value = goalFats.toFloat()
    }

    /**
     * Update the string value of the dialog for protein
     */
    fun updateDialogProtein(newProteinValue: Float) {
        _dialogProteinValue.value = newProteinValue
    }

    /**
     * Update the string value of the dialog for carbs
     */
    fun updateDialogCarbohydrates(newCarbohydratesValue: Float) {
        _dialogCarbohydratesValue.value = newCarbohydratesValue
    }

    /**
     * Update the string value of the dialog for fats
     */
    fun updateDialogFats(newFatsValue: Float) {
        _dialogFatsValue.value = newFatsValue
    }

    /**
     * Get the maximum possible protein given calories
     */
    fun getMaxProtein(): Float {
        return 500f
    }

    /**
     * Get the maximum possible carbs given calories
     */
    fun getMaxCarbs(): Float {
        return 1000f
    }

    /**
     * Get the maximum possible fats given calories
     */
    fun getMaxFats(): Float {
        return 250f
    }

    /**
     * Updates the goals of the user's calories and macros to what is in the dialog if valid
     */
    fun updateGoals(): Boolean {
        mealViewModel.updateProteinGoal(dialogProteinValue.toInt())
        mealViewModel.updateCarbohydratesGoal(dialogCarbohydratesValue.toInt())
        mealViewModel.updateFatsGoal(dialogFatsValue.toInt())

        return true
    }

    /**
     * Clears the dialog box
     */
    fun clearDialog() {
    }
}