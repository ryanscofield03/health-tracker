package com.healthtracking.app.viewmodels.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.daos.MealDao
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.Meal
import com.healthtracking.app.entities.MealFoodCrossRef
import com.healthtracking.app.entities.MealWithFoodList
import com.healthtracking.app.services.toDecimalPoints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class BuildMealViewModel(
    private val mealDao: MealDao
): ViewModel() {
    companion object {
        val MEASUREMENT_OPTIONS = listOf(
            "1 unit",
            "1 cup",
            "100 g",
            "50 g",
            "1 tbsp"
        )

        private val DEFAULT_MEASUREMENT = MEASUREMENT_OPTIONS[0]
        private const val DEFAULT_PROTEIN = 0f
        private const val DEFAULT_CARBS = 0f
        private const val DEFAULT_FATS = 0f
        private const val DEFAULT_QUANTITY = 1f
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

    private val _dialogMeasurement = MutableStateFlow(DEFAULT_MEASUREMENT)
    val dialogMeasurement get() = _dialogMeasurement

    private val _dialogProtein = MutableStateFlow(DEFAULT_PROTEIN)
    val dialogProtein get() = _dialogProtein

    private val _dialogCarbs = MutableStateFlow(DEFAULT_CARBS)
    val dialogCarbs get() = _dialogCarbs

    private val _dialogFats = MutableStateFlow(DEFAULT_FATS)
    val dialogFats get() = _dialogFats

    private val _dialogQuantity = MutableStateFlow(DEFAULT_QUANTITY)
    val dialogQuantity get() = _dialogQuantity

    val dialogCalories get() =
        ((dialogProtein.value * 4 + dialogCarbs.value * 4 + dialogFats.value * 9)).toDecimalPoints(0)

    private val _editMealId: MutableState<Long?> = mutableStateOf(null)
    val editMealId: Long? get() = _editMealId.value

    /**
     * Initialize data when editing an old entry
     */
    fun editMealInfo(mealWithFoodList: MealWithFoodList) {
        _editMealId.value = mealWithFoodList.meal.id
        reuseMealInfo(mealWithFoodList = mealWithFoodList)
    }

    /**
     * Initialize data when reusing an old entry
     */
    fun reuseMealInfo(mealWithFoodList: MealWithFoodList) {
        _name.value = mealWithFoodList.meal.name
        _foodItems.value = mealWithFoodList.foodItems
    }

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

            clearFoodDialog()
        }

    }

    /**
     * Clears the food entry dialog (e.g. cancels or saves)
     */
    fun clearFoodDialog() {
        _dialogFoodName.value = null
        _dialogMeasurement.value = DEFAULT_MEASUREMENT
        _dialogProtein.value = DEFAULT_PROTEIN
        _dialogCarbs.value = DEFAULT_CARBS
        _dialogFats.value = DEFAULT_FATS
        _dialogQuantity.value = DEFAULT_QUANTITY
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

        viewModelScope.launch(Dispatchers.IO) {
            // TODO handle case where editing is true and meal already exists

            val mealId = mealDao.upsertMealEntity(
                mealEntity = Meal(
                    name = _name.value,
                    date = LocalDate.now(),
                    time = LocalTime.now()
                )
            )

            _foodItems.value.forEach { foodItem: Food ->
                val foodId = mealDao.upsertFoodEntity(foodEntity = foodItem)
                val mealFoodCrossRef = MealFoodCrossRef(
                    mealId = mealId,
                    foodId = foodId
                )
                mealDao.upsertMealFoodCrossRef(crossRef = mealFoodCrossRef)
            }
        }
    }

    /**
     * Clears the view model
     */
    fun clear() {
        _editMealId.value = null
        _name.value = ""
        _foodItems.value = listOf()
    }
}