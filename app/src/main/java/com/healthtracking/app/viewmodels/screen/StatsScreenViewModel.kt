package com.healthtracking.app.viewmodels.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.ExerciseHistory
import com.healthtracking.app.entities.MealWithFoodList
import com.healthtracking.app.entities.Sleep
import com.healthtracking.app.entities.WorkoutHistory
import com.healthtracking.app.services.calculateTimeSleptFloat
import com.healthtracking.app.services.toDecimalPoints
import com.healthtracking.app.viewmodels.database.ExerciseHistoryViewModel
import com.healthtracking.app.viewmodels.database.ExerciseViewModel
import com.healthtracking.app.viewmodels.database.MealViewModel
import com.healthtracking.app.viewmodels.database.SleepViewModel
import com.healthtracking.app.viewmodels.database.WorkoutHistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class StatsScreenViewModel(
    private val exerciseViewModel: ExerciseViewModel,
    private val workoutHistoryViewModel: WorkoutHistoryViewModel,
    private val exerciseHistoryViewModel: ExerciseHistoryViewModel,
    private val mealViewModel: MealViewModel,
    private val sleepViewModel: SleepViewModel
): ViewModel() {
    private val _sleepData: MutableStateFlow<List<Sleep>?> = MutableStateFlow(emptyList())
    private val _workoutHistoryData: MutableStateFlow<List<WorkoutHistory>?> = MutableStateFlow(emptyList())
    private val _exerciseHistoryData: MutableStateFlow<List<ExerciseHistory>?> = MutableStateFlow(emptyList())
    private val _mealData: MutableStateFlow<List<MealWithFoodList>?> = MutableStateFlow(emptyList())

    private val _selectedExercise: MutableStateFlow<String?> = MutableStateFlow(null)
    val selectedExercise get() = _selectedExercise

    val caloriesGoal get() = mealViewModel.goalCalories
    val proteinGoal get() = mealViewModel.goalProtein
    val carbsGoal get() = mealViewModel.goalCarbohydrates
    val fatsGoal get() = mealViewModel.goalFats

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mealViewModel.allMeals.collect{ _mealData.value = it }
        }
        viewModelScope.launch(Dispatchers.IO) {
            sleepViewModel.getSleepEntriesFlow().collect{ _sleepData.value = it }
        }
        viewModelScope.launch(Dispatchers.IO) {
            workoutHistoryViewModel.allWorkoutHistory.collect{ _workoutHistoryData.value = it }
        }
    }

    fun getSleepHoursData(): Map<LocalDate, Float> {
        return _sleepData.value?.associateBy(
            { it.date },
            {
                calculateTimeSleptFloat(startTime = it.startTime, endTime = it.endTime)
                    .toDecimalPoints(1)
            }
        )
            ?: mapOf(Pair(LocalDate.now(), 0f))
    }

    fun getSleepRatingsData(): Map<LocalDate, Float> {
        return _sleepData.value?.associateBy(
            { it.date },
            { it.rating.toFloat() }
        )
            ?: mapOf(Pair(LocalDate.now(), 0f))
    }

    fun getCaloriesData(): Flow<Map<LocalDate, Double>> {
        val data = _mealData.map { mealsList ->
            mealsList
                ?.groupBy { it.meal.date }
                ?.mapValues { entry ->
                    entry.value.map { meal: MealWithFoodList ->
                        if (meal.foodItems.isNotEmpty()) {
                            meal.foodItems.map { food -> food.calories }.sumOf { it.toDouble() }
                        } else {
                            0f
                        }
                    }.sumOf { it.toDouble() }
                } ?: mapOf()
        }

        viewModelScope.launch {
            println(_mealData.value)
            data.collect { println(it) }
        }
        return data
    }

    fun getProteinData(): Flow<Map<LocalDate, Double>> {
        return _mealData.map { mealsList ->
            mealsList
                ?.groupBy { it.meal.date }
                ?.mapValues { entry ->
                    entry.value.map { meal: MealWithFoodList ->
                        if (meal.foodItems.isNotEmpty()) {
                            meal.foodItems.map { food -> food.protein }.sumOf { it.toDouble() }
                        } else {
                            0f
                        }
                    }.sumOf { it.toDouble() }
                } ?: mapOf()
        }
    }

    fun getCarbsData(): Flow<Map<LocalDate, Double>> {
        return _mealData.map { mealsList ->
            mealsList
                ?.groupBy { it.meal.date }
                ?.mapValues { entry ->
                    entry.value.map { meal: MealWithFoodList ->
                        if (meal.foodItems.isNotEmpty()) {
                            meal.foodItems.map { food -> food.carbohydrates }.sumOf { it.toDouble() }
                        } else {
                            0f
                        }
                    }.sumOf { it.toDouble() }
                } ?: mapOf()
        }
    }

    fun getFatsData(): Flow<Map<LocalDate, Double>> {
        return _mealData.map { mealsList ->
            mealsList
                ?.groupBy { it.meal.date }
                ?.mapValues { entry ->
                    entry.value.map { meal: MealWithFoodList ->
                        if (meal.foodItems.isNotEmpty()) {
                            meal.foodItems.map { food -> food.fats }.sumOf { it.toDouble() }
                        } else {
                            0f
                        }
                    }.sumOf { it.toDouble() }
                } ?: mapOf()
        }
    }

    fun getWorkoutAttendance(): Float {
        return if (_workoutHistoryData.value != null && _workoutHistoryData.value!!.isNotEmpty()) {
            val firstDate = _workoutHistoryData.value!!.minOf { it.date }
            val lastDate = LocalDate.now()

            val daysSinceFirstDate = lastDate.toEpochDay() - firstDate.toEpochDay()
            val workoutsAttended = _workoutHistoryData.value!!.size
            (workoutsAttended / (daysSinceFirstDate/7 + 1)).toFloat()
        } else {
            0f
        }
    }

    fun getWorkoutHistory(): StateFlow<List<WorkoutHistory>?> {
        viewModelScope.launch {
            workoutHistoryViewModel.allWorkoutHistory.collect{ _workoutHistoryData.value = it }
        }

        return _workoutHistoryData
    }

    fun getExercises(): StateFlow<List<Exercise>?> {
        return exerciseViewModel.allExercises.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = listOf()
        )
    }

    fun updateSelectedExercise(exercise: String) {
        _selectedExercise.value = exercise

        viewModelScope.launch {
            exerciseHistoryViewModel.getExerciseHistory(exerciseName = exercise)
                .collect{ _exerciseHistoryData.value = it }
        }
    }

    fun getSelectedExerciseWeightData(): Map<LocalDate, List<Float>> {
        return _exerciseHistoryData.value?.associateBy(
            { it.date.toLocalDate() },
            { it.data.map { weightSetPair -> weightSetPair.first } }
        ) ?: mapOf(Pair(LocalDate.now(), listOf(0f)))
    }

    fun getSelectedExerciseRepData(): Map<LocalDate, List<Float>> {
        return _exerciseHistoryData.value?.associateBy(
            { it.date.toLocalDate() },
            { it.data.map { weightSetPair -> weightSetPair.second.toFloat() } }
        ) ?: mapOf(Pair(LocalDate.now(), listOf(0f)))
    }
}