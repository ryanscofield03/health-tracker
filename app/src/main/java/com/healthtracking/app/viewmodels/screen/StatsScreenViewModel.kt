package com.healthtracking.app.viewmodels.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.daos.MealDao
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.ExerciseHistory
import com.healthtracking.app.entities.Sleep
import com.healthtracking.app.entities.WorkoutHistory
import com.healthtracking.app.services.calculateTimeSleptFloat
import com.healthtracking.app.services.toDecimalPoints
import com.healthtracking.app.viewmodels.database.ExerciseHistoryViewModel
import com.healthtracking.app.viewmodels.database.ExerciseViewModel
import com.healthtracking.app.viewmodels.database.MealViewModel
import com.healthtracking.app.viewmodels.database.SleepViewModel
import com.healthtracking.app.viewmodels.database.WorkoutHistoryViewModel
import com.healthtracking.app.viewmodels.database.WorkoutViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class StatsScreenViewModel(
    private val workoutViewModel: WorkoutViewModel,
    private val exerciseViewModel: ExerciseViewModel,
    private val workoutHistoryViewModel: WorkoutHistoryViewModel,
    private val exerciseHistoryViewModel: ExerciseHistoryViewModel,
    private val mealViewModel: MealViewModel,
    private val mealDao: MealDao,
    private val sleepViewModel: SleepViewModel
): ViewModel() {
    private val _sleepData: MutableStateFlow<List<Sleep>?> = MutableStateFlow(emptyList())
    private val _workoutHistoryData: MutableStateFlow<List<WorkoutHistory>?> = MutableStateFlow(emptyList())
    private val _exerciseHistoryData: MutableStateFlow<List<ExerciseHistory>?> = MutableStateFlow(emptyList())

    private val _selectedExercise: MutableStateFlow<String?> = MutableStateFlow(null)
    val selectedExercise get() = _selectedExercise

    init {
        viewModelScope.launch {
            sleepViewModel.getSleepEntriesFlow().collect{ _sleepData.value = it }
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

    fun getCaloriesData(): Map<LocalDate, Double> {
        return mapOf(
            Pair(LocalDate.now().minusDays(13), 2300.0),
            Pair(LocalDate.now().minusDays(12), 2700.0),
            Pair(LocalDate.now().minusDays(11), 2500.0),
            Pair(LocalDate.now().minusDays(10), 2300.0),
            Pair(LocalDate.now().minusDays(9), 2350.0),
            Pair(LocalDate.now().minusDays(8), 2550.0),
            Pair(LocalDate.now().minusDays(7), 2400.0),
            Pair(LocalDate.now().minusDays(6), 2600.0),
            Pair(LocalDate.now().minusDays(5), 2200.0),
            Pair(LocalDate.now().minusDays(4), 2600.0),
            Pair(LocalDate.now().minusDays(3), 2250.0),
            Pair(LocalDate.now().minusDays(2), 2550.0),
            Pair(LocalDate.now().minusDays(1), 2450.0),
            Pair(LocalDate.now(), 2300.0)
        )
    }

    fun getProteinData(): Map<LocalDate, Double> {
        return mapOf(
            Pair(LocalDate.now().minusDays(13), 75.0),
            Pair(LocalDate.now().minusDays(12), 120.0),
            Pair(LocalDate.now().minusDays(11), 75.0),
            Pair(LocalDate.now().minusDays(10), 110.0),
            Pair(LocalDate.now().minusDays(9), 95.0),
            Pair(LocalDate.now().minusDays(8), 70.0),
            Pair(LocalDate.now().minusDays(7), 75.0),
            Pair(LocalDate.now().minusDays(6), 80.0),
            Pair(LocalDate.now().minusDays(5), 65.0),
            Pair(LocalDate.now().minusDays(4), 100.0),
            Pair(LocalDate.now().minusDays(3), 85.0),
            Pair(LocalDate.now().minusDays(2), 80.0),
            Pair(LocalDate.now().minusDays(1), 95.0),
            Pair(LocalDate.now(), 100.0)
        )
    }

    fun getCarbsData(): Map<LocalDate, Double> {
        return mapOf(
            Pair(LocalDate.now().minusDays(13), 500.0),
            Pair(LocalDate.now().minusDays(12), 350.0),
            Pair(LocalDate.now().minusDays(11), 300.0),
            Pair(LocalDate.now().minusDays(10), 350.0),
            Pair(LocalDate.now().minusDays(9), 500.0),
            Pair(LocalDate.now().minusDays(8), 200.0),
            Pair(LocalDate.now().minusDays(7), 350.0),
            Pair(LocalDate.now().minusDays(6), 300.0),
            Pair(LocalDate.now().minusDays(5), 400.0),
            Pair(LocalDate.now().minusDays(4), 250.0),
            Pair(LocalDate.now().minusDays(3), 500.0),
            Pair(LocalDate.now().minusDays(2), 350.0),
            Pair(LocalDate.now().minusDays(1), 400.0),
            Pair(LocalDate.now(), 350.0)
        )
    }

    fun getFatsData(): Map<LocalDate, Double> {
        return mapOf(
            Pair(LocalDate.now().minusDays(13), 30.0),
            Pair(LocalDate.now().minusDays(12), 40.0),
            Pair(LocalDate.now().minusDays(11), 30.0),
            Pair(LocalDate.now().minusDays(10), 20.0),
            Pair(LocalDate.now().minusDays(9), 50.0),
            Pair(LocalDate.now().minusDays(8), 40.0),
            Pair(LocalDate.now().minusDays(7), 30.0),
            Pair(LocalDate.now().minusDays(6), 40.0),
            Pair(LocalDate.now().minusDays(5), 45.0),
            Pair(LocalDate.now().minusDays(4), 35.0),
            Pair(LocalDate.now().minusDays(3), 20.0),
            Pair(LocalDate.now().minusDays(2), 50.0),
            Pair(LocalDate.now().minusDays(1), 40.0),
            Pair(LocalDate.now(), 35.0)
        )
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

    fun getWorkoutHistory(): List<WorkoutHistory> {
        return if (_workoutHistoryData.value != null && _workoutHistoryData.value!!.isNotEmpty()) {
            _workoutHistoryData.value!!
        } else {
            return listOf()
        }
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