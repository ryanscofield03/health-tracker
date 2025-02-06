package com.healthtracking.app.viewmodels.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.daos.MealDao
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.Meal
import com.healthtracking.app.services.toDecimalPoints
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.math.roundToInt

class BuildMealViewModel(
    private val mealDao: MealDao
): ViewModel() {
    companion object {
        val MEASUREMENT_OPTIONS = listOf(
            "1 Unit",
            "1 Cup"
        )
    }

    // store meal name
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    // store list of food items
    private val _foodItems = MutableStateFlow<List<Food>>(emptyList())
    val foodItems: StateFlow<List<Food>> = _foodItems.asStateFlow()

    // store error messages
    private val _nameErrorMessageId = MutableStateFlow<Int?>(null)
    val nameErrorMessageId: StateFlow<Int?> = _nameErrorMessageId.asStateFlow()

    private val _foodItemsErrorMessageId = MutableStateFlow<Int?>(null)
    val foodItemsErrorMessageId: StateFlow<Int?> = _foodItemsErrorMessageId.asStateFlow()

    private val _dialogFoodName = MutableStateFlow<String?>(null)
    val dialogFoodName get() = _dialogFoodName

    private val _dialogMeasurement = MutableStateFlow(MEASUREMENT_OPTIONS[0])
    val dialogMeasurement get() = _dialogMeasurement

    private val _dialogProtein = MutableStateFlow(0f)
    val dialogProtein get() = _dialogProtein

    private val _dialogCarbs = MutableStateFlow(0f)
    val dialogCarbs get() = _dialogCarbs

    private val _dialogFats = MutableStateFlow(0f)
    val dialogFats get() = _dialogFats

    private val _dialogQuantity = MutableStateFlow(1f)
    val dialogQuantity get() = _dialogQuantity

    val dialogCalories get() =
        ((dialogProtein.value * 4 + dialogCarbs.value * 4 + dialogFats.value * 9) * dialogQuantity.value).toDecimalPoints(0)

    /**
     * Updates the name of the food item
     */
    fun updateDialogFoodName(newName: String) {
        _dialogFoodName.value = newName
    }

    /**
     * Updates the measurement of the food item
     */
    fun updateDialogMeasurement(newMeasurement: String) {
        _dialogMeasurement.value = newMeasurement
    }

    /**
     * Updates the protein value of the food item
     */
    fun updateDialogProtein(newProtein: Float) {
        _dialogProtein.value = newProtein.toDecimalPoints(1)
    }

    /**
     * Updates the carbs value of the food item
     */
    fun updateDialogCarbs(newCarbs: Float) {
        _dialogCarbs.value = newCarbs.toDecimalPoints(1)
    }

    /**
     * Updates the fats value of the food item
     */
    fun updateDialogFats(newFats: Float) {
        _dialogFats.value = newFats.toDecimalPoints(1)
    }

    /**
     * Updates the quantity of the food item
     */
    fun updateDialogQuantity(newQuantity: Float) {
        _dialogQuantity.value = newQuantity.toDecimalPoints(0)
    }

    /**
     * Updates the name of the meal
     */
    fun updateName(name: String) {
        _name.value = name
    }

    /**
     * Return true if food dialog has all valid data
     */
    fun validFoodDialog(): Boolean {
        return dialogFoodName.value != null && dialogFoodName.value!!.isNotBlank()
    }

    /**
     * Adds a food item to the list of food items
     */
    fun addFoodItem() {
        if (validFoodDialog()) {
            val foodItem = Food(
                name = dialogFoodName.value!!,
                measurement = dialogMeasurement.value,
                calories = dialogCalories,
                protein = dialogProtein.value,
                carbohydrates = dialogCarbs.value,
                fats = dialogFats.value,
                quantity = dialogQuantity.value
            )

            _foodItems.value += foodItem
        }

    }

    /**
     * Removes a given food item from list of food items
     */
    fun removeFoodItem(foodItem: Food) {
        _foodItems.value -= foodItem
    }

    /**
     * Get a list of foods that match the user's inputted name
     * e.g. eggs -> 1 egg, 100g, 1 cup, etc (and have attached macros + calories)
     */
    fun getNutritionalDataList(foodName: String): List<Food> {
        return listOf(
            Food(
                name = "egg",
                calories = 155f,
                measurement = "1 Unit",
                protein = 12f,
                carbohydrates = 15f,
                fats = 10f,
                quantity = 1f
            )
        )
    }

    /**
     * Validate meal name and items (e.g. has at least 1 item)
     */
    fun validateMeal(): Boolean {
        return _name.value.isNotBlank() && _foodItems.value.isNotEmpty()
    }

    /**
     * Save all data into meal and food entities
     */
    fun save() {
        if (!validateMeal()) return

        viewModelScope.launch {
            mealDao.upsertMealEntity(
                Meal(
                    name = _name.value,
                    date = LocalDateTime.now()
                )
            )

//            _foodItems.value.forEach { foodItem ->
//                mealDao.insertFood(mealId = mealId, foodItem = foodItem)
//            }
        }
    }

    /**
     * Clears the view model
     */
    fun clear() {
        _name.value = ""
        _foodItems.value = listOf()
    }
}