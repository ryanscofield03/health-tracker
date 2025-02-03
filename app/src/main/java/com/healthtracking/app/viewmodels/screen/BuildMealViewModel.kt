package com.healthtracking.app.viewmodels.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.daos.MealDao
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.Meal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BuildMealViewModel(
    private val mealDao: MealDao
): ViewModel() {
    // store meal name
    private val _mealName = MutableStateFlow("")
    val mealName: StateFlow<String> = _mealName.asStateFlow()

    // store list of food items
    private val _foodItems = MutableStateFlow<List<Food>>(emptyList())
    val foodItems: StateFlow<List<Food>> = _foodItems.asStateFlow()

    /**
     * Get a list of foods that match the user's inputted name
     * e.g. eggs -> 1 egg, 100g, 1 cup, etc (and have attached macros + calories)
     */
    fun getNutritionalDataList(foodName: String): List<Food> {
        return listOf(
            Food(
                name = "egg",
                calories = 155f,
                protein = 12f,
                carbohydrates = 15f,
                fats = 10f
            )
        )
    }

    /**
     * Validate meal name and items (e.g. has at least 1 item)
     */
    fun validateMeal(): Boolean {
        return _mealName.value.isNotBlank() && _foodItems.value.isNotEmpty()
    }

    /**
     * save all data into meal and food entities
     */
    fun saveMeal() {
        if (!validateMeal()) return

        viewModelScope.launch {
            mealDao.upsertMealEntity(
                Meal(
                    name = _mealName.value,
                    date = LocalDateTime.now()
                )
            )

//            _foodItems.value.forEach { foodItem ->
//                mealDao.insertFood(mealId = mealId, foodItem = foodItem)
//            }
        }
    }
}