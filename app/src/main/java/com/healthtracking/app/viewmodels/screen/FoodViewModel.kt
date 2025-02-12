package com.healthtracking.app.viewmodels.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.MealWithFoodList
import com.healthtracking.app.services.toDecimalPoints
import com.healthtracking.app.viewmodels.database.MealViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class FoodViewModel(private val mealViewModel: MealViewModel): ViewModel() {
    val currentCalories: Flow<Float> get() = calculateCurrentCalories()
    val currentProtein: Flow<Float> get() = calculateCurrentProtein()
    val currentCarbohydrates: Flow<Float> get() = calculateCurrentCarbohydrates()
    val currentFats: Flow<Float> get() = calculateCurrentFats()

    val weeklyCaloriesPercent: Flow<Float> get() = calculateWeeklyCaloriesProgress()
    val weeklyProteinPercent: Flow<Float> get() = calculateWeeklyProteinProgress()
    val weeklyCarbohydratesPercent: Flow<Float> get() = calculateWeeklyCarbohydratesProgress()
    val weeklyFatsPercent: Flow<Float> get() = calculateWeeklyFatsProgress()

    val goalCalories get() = mealViewModel.goalCalories
    val goalProtein get() = mealViewModel.goalProtein
    val goalCarbohydrates get() = mealViewModel.goalCarbohydrates
    val goalFats get() = mealViewModel.goalFats

    val dialogCaloriesGoal: String get() = dialogProteinValue.toInt().times(4)
        .plus(dialogCarbohydratesValue.toInt().times(4))
        .plus(dialogFatsValue.toInt().times(9)).toString()

    private val _dialogProteinValue: MutableState<Float> = mutableFloatStateOf(goalProtein.value.toFloat())
    val dialogProteinValue: Float get() = _dialogProteinValue.value

    private val _dialogCarbohydratesValue: MutableState<Float> = mutableFloatStateOf(goalCarbohydrates.value.toFloat())
    val dialogCarbohydratesValue: Float get() = _dialogCarbohydratesValue.value

    private val _dialogFatsValue: MutableState<Float> = mutableFloatStateOf(goalFats.value.toFloat())
    val dialogFatsValue: Float get() = _dialogFatsValue.value

    private val _currentMealEntries: MutableStateFlow<List<MealWithFoodList>?> = MutableStateFlow(listOf())
    val currentMealEntries: StateFlow<List<MealWithFoodList>?> = _currentMealEntries

    private val _weeklyMealEntries: MutableStateFlow<List<MealWithFoodList>?> = MutableStateFlow(listOf())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mealViewModel.getTodaysMealEntries().collect {
                _currentMealEntries.value = it
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            mealViewModel.getWeeklyMealEntries().collect {
                _weeklyMealEntries.value = it
            }
        }
    }

    /**
     * Deletes the meal from the database where id = mealId
     */
    fun deleteMeal(mealId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            mealViewModel.deleteMeal(mealId)
        }
    }

    /**
     * Calculate the current calories consumed by the user on this day
     */
    private fun calculateCurrentCalories(): Flow<Float> {
        return _currentMealEntries.map { mealList: List<MealWithFoodList>? ->
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
        return _currentMealEntries.map { mealList: List<MealWithFoodList>? ->
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
        return _currentMealEntries.map { mealList: List<MealWithFoodList>? ->
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
        return _currentMealEntries.map { mealList: List<MealWithFoodList>? ->
            mealList?.map { mealWithFood: MealWithFoodList ->
                mealWithFood.foodItems.map { foodItem: Food ->
                    foodItem.fats.times(foodItem.quantity)
                }
            }?.flatten()?.sum() ?: 0f
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 1)
    }

    /**
     * Returns a StateFlow of the weekly calories progress (percent of calories met this week)
     */
    private fun calculateWeeklyCaloriesProgress(): Flow<Float> {
        return _weeklyMealEntries.map { mealList: List<MealWithFoodList>? ->
            (mealList?.map { mealWithFood: MealWithFoodList ->
                mealWithFood.foodItems.map { foodItem: Food ->
                    foodItem.calories.times(foodItem.quantity)
                }
            }?.flatten()?.sum()?.div(goalCalories.value.times(7))?.times(100)) ?: 0f
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 1)
    }

    /**
     * Returns a StateFlow of the weekly protein progress (percent of calories met this week)
     */
    private fun calculateWeeklyProteinProgress(): Flow<Float> {
        return _weeklyMealEntries.map { mealList: List<MealWithFoodList>? ->
            (mealList?.map { mealWithFood: MealWithFoodList ->
                mealWithFood.foodItems.map { foodItem: Food ->
                    foodItem.protein.times(foodItem.quantity)
                }
            }?.flatten()?.sum()?.div(goalProtein.value.times(7))?.times(100)) ?: 0f
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 1)
    }

    /**
     * Returns a StateFlow of the weekly carbs progress (percent of calories met this week)
     */
    private fun calculateWeeklyCarbohydratesProgress(): Flow<Float> {
        return _weeklyMealEntries.map { mealList: List<MealWithFoodList>? ->
            (mealList?.map { mealWithFood: MealWithFoodList ->
                mealWithFood.foodItems.map { foodItem: Food ->
                    foodItem.carbohydrates.times(foodItem.quantity)
                }
            }?.flatten()?.sum()?.div(goalCarbohydrates.value.times(7))?.times(100)) ?: 0f
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 1)
    }

    /**
     * Returns a StateFlow of the weekly calories progress (percent of calories met this week)
     */
    private fun calculateWeeklyFatsProgress(): Flow<Float> {
        return _weeklyMealEntries.map { mealList: List<MealWithFoodList>? ->
            (mealList?.map { mealWithFood: MealWithFoodList ->
                mealWithFood.foodItems.map { foodItem: Food ->
                    foodItem.fats.times(foodItem.quantity)
                }
            }?.flatten()?.sum()?.div(goalFats.value.times(7))?.times(100)) ?: 0f
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 1)
    }

    /**
     * Populate the dialog box with the current goals
     */
    fun populateDialogEntries() {
        _dialogProteinValue.value = goalProtein.value.toFloat()
        _dialogCarbohydratesValue.value = goalCarbohydrates.value.toFloat()
        _dialogFatsValue.value = goalFats.value.toFloat()
    }

    /**
     * Update the string value of the dialog for protein
     */
    fun updateDialogProtein(newProteinValue: Float) {
        _dialogProteinValue.value = newProteinValue.toDecimalPoints(0)
    }

    /**
     * Update the string value of the dialog for carbs
     */
    fun updateDialogCarbohydrates(newCarbohydratesValue: Float) {
        _dialogCarbohydratesValue.value = newCarbohydratesValue.toDecimalPoints(0)
    }

    /**
     * Update the string value of the dialog for fats
     */
    fun updateDialogFats(newFatsValue: Float) {
        _dialogFatsValue.value = newFatsValue.toDecimalPoints(0)
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