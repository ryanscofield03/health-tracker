package com.healthtracking.app.viewmodels.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.R
import com.healthtracking.app.daos.MealDao
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.Meal
import com.healthtracking.app.entities.MealFoodCrossRef
import com.healthtracking.app.entities.MealWithFoodList
import com.healthtracking.app.services.nutritiondata.NutritionalDataAPI
import com.healthtracking.app.services.toDecimalPoints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
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

        enum class EntryStates {
            NEW,
            EDIT,
            REUSE
        }

        private val DEFAULT_MEASUREMENT = MEASUREMENT_OPTIONS[0]
        private const val DEFAULT_PROTEIN = 0f
        private const val DEFAULT_CARBS = 0f
        private const val DEFAULT_FATS = 0f
        private const val DEFAULT_QUANTITY = 1f
        private val DEFAULT_ENTRY_MODE = EntryStates.NEW
    }

    private val nutritionalDataAPI: NutritionalDataAPI = NutritionalDataAPI()

    private val _mealSearch = MutableStateFlow("")
    val mealSearch: StateFlow<String> = _mealSearch.asStateFlow()

    private val _allMeals = MutableStateFlow<List<MealWithFoodList>>(emptyList())

    // store meal name
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    // store list of food items
    private val _foodItems = MutableStateFlow<List<Food>>(emptyList())
    val foodItems: StateFlow<List<Food>> = _foodItems.asStateFlow()

    private val _entryMode = mutableStateOf(DEFAULT_ENTRY_MODE)
    // for comparing to when saving
    private val _originalEntry: MutableState<MealWithFoodList?> = mutableStateOf(null)

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

    private val _dialogEntryMode: MutableState<EntryStates> = mutableStateOf(DEFAULT_ENTRY_MODE)
    private val _editFoodIndex: MutableState<Int?> = mutableStateOf(null)

    val isLoadingApiData: MutableState<Boolean> = mutableStateOf(false)

    private val _alreadySaved: MutableState<Boolean> = mutableStateOf(false)
    val alreadySaved: Boolean get() = _alreadySaved.value

    private val _alreadySavedDialog: MutableState<Boolean> = mutableStateOf(false)
    val alreadySavedDialog: Boolean get() = _alreadySavedDialog.value

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mealDao.getAllMealEntries().collect { _allMeals.value  = it ?: listOf() }
        }
    }

    /**
     * Initialize data when editing an old entry
     */
    fun editMealInfo(mealWithFoodList: MealWithFoodList) {
        _entryMode.value = EntryStates.EDIT
        _originalEntry.value = mealWithFoodList
        updateName(mealWithFoodList.meal.name)
        _foodItems.value = mealWithFoodList.foodItems
    }

    /**
     * Initialize data when reusing an old entry
     */
    fun reuseMealInfo(mealWithFoodList: MealWithFoodList) {
        _entryMode.value = EntryStates.REUSE
        _originalEntry.value = mealWithFoodList
        updateName(mealWithFoodList.meal.name)
        _foodItems.value = mealWithFoodList.foodItems
    }

    /**
     * Populate dialog with food data and set entry state to edit
     */
    fun populateDialogWithFood(food: Food) {
        updateDialogFoodName(food.name)
        updateDialogMeasurement(food.measurement)
        updateDialogProtein(food.protein)
        updateDialogCarbs(food.carbohydrates)
        updateDialogFats(food.fats)
        updateDialogQuantity(food.quantity)
        _editFoodIndex.value = _foodItems.value.indexOf(food)
        _dialogEntryMode.value = EntryStates.EDIT
    }

    fun updateMealSearch(newMealSearch: String) {
        _mealSearch.value = newMealSearch
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
        if (newQuantity >= 1) {
            _dialogQuantity.value = newQuantity.toDecimalPoints(0)
        }
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
        _alreadySavedDialog.value = true

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

            when (_dialogEntryMode.value) {
                EntryStates.NEW -> {
                    _foodItems.value += foodItem

                    clearFoodDialog()
                }
                EntryStates.EDIT -> {
                    _foodItems.value = foodItems.value.toMutableList().apply {
                        this[_editFoodIndex.value!!] = foodItem
                    }
                    clearFoodDialog()
                }

                EntryStates.REUSE -> {
                    // NOT POSSIBLE AS OF CURRENT
                }
            }
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
        _dialogEntryMode.value = DEFAULT_ENTRY_MODE
        _alreadySavedDialog.value = false
    }

    /**
     * Removes a given food item from list of food items
     */
    fun removeFoodItem(foodItem: Food) {
        _foodItems.value -= foodItem
    }

    /**
     * Get and populate dialog fields with nutritional data
     */
    fun populateDialogWithFoodNutrients() {
        if (_dialogFoodName.value == null || _dialogFoodName.value!!.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            isLoadingApiData.value = true

            val foodData = nutritionalDataAPI.getNutritionalData(
                foodItem = _dialogFoodName.value!!,
                measurement = _dialogMeasurement.value
            )

            if (foodData != null) {
                _dialogProtein.value = foodData.protein
                _dialogCarbs.value = foodData.carbohydrates
                _dialogFats.value = foodData.fats
            }

            isLoadingApiData.value = false
        }
    }

    fun mealsFilteredBySearch(): StateFlow<List<MealWithFoodList>> {
        return _allMeals.map { meals ->
            meals.filter { it.meal.name.contains(_mealSearch.value) ||
                    it.foodItems.any { food -> food.name.contains(_mealSearch.value) }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    /**
     * true if meal name and items are valid (e.g. has at least 1 item, name not blank)
     */
    fun validMeal(): Boolean {
        return if (_name.value.isBlank()) {
            false
        } else if (_foodItems.value.isEmpty()) {
            false
        } else {
            true
        }
    }

    /**
     * Save all data into meal and food entities
     */
    fun save() {
        _alreadySaved.value = true

        if (!validMeal()) return

        viewModelScope.launch(Dispatchers.IO) {
            when (_entryMode.value) {
                EntryStates.NEW -> {
                    saveNewEntry()
                }
                EntryStates.EDIT -> {
                    saveEditedEntry()
                }
                EntryStates.REUSE -> {
                    saveReusedEntry()
                }
            }
        }
    }

    /**
     * Save all new data into meal and food entities
     */
    private suspend fun saveNewEntry() {
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

    /**
     * Save all new data into meal and food entities but update foodItems to use new id
     */
    private suspend fun saveReusedEntry() {
        val mealId = mealDao.upsertMealEntity(
            mealEntity = Meal(
                name = _name.value,
                date = LocalDate.now(),
                time = LocalTime.now()
            )
        )

        _foodItems.value.forEach { foodItem: Food ->
            // TODO - perhaps check if this has changed and reuse the same entity if it hasn't changed
            //  (especially makes sense when quantity is moved to cross ref as 100g of egg always
            //  has the same nutrients)
            val foodId = mealDao.upsertFoodEntity(foodEntity = Food(
                name = foodItem.name,
                measurement = foodItem.measurement,
                calories = foodItem.calories,
                protein = foodItem.protein,
                fats = foodItem.fats,
                carbohydrates = foodItem.carbohydrates,
                quantity = foodItem.quantity)
            )
            val mealFoodCrossRef = MealFoodCrossRef(
                mealId = mealId,
                foodId = foodId
            )
            mealDao.upsertMealFoodCrossRef(crossRef = mealFoodCrossRef)
        }
    }

    /**
     * Save updated data
     */
    private suspend fun saveEditedEntry() {
        val updatedFoodItems = _foodItems.value

        mealDao.upsertMealEntity(
            mealEntity = _originalEntry.value!!.meal.copy(name = _name.value)
        )

        updatedFoodItems.forEach { foodItem: Food ->
            if (foodItem.id != 0L) {
                // update old entry
                mealDao.upsertFoodEntity(foodEntity = foodItem)
            } else {
                // add new entry
                val foodId = mealDao.upsertFoodEntity(foodEntity = foodItem)
                val mealFoodCrossRef = MealFoodCrossRef(
                    mealId = _originalEntry.value!!.meal.id,
                    foodId = foodId
                )
                mealDao.upsertMealFoodCrossRef(crossRef = mealFoodCrossRef)
            }
        }

        // Remove deleted food items
        _originalEntry.value!!.foodItems.forEach { foodItem: Food ->
            if (updatedFoodItems.none { it.id == foodItem.id }) {
                mealDao.deleteFood(foodId = foodItem.id)
            }
        }
    }

    /**
     * Clears the view model
     */
    fun clear() {
        _alreadySaved.value = false
        _entryMode.value = EntryStates.NEW
        _name.value = ""
        _foodItems.value = listOf()
    }
}