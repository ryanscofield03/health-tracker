package com.healthtracking.app.viewmodels.database

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.daos.MealDao
import com.healthtracking.app.entities.MealWithFoodList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.time.LocalDate

class MealViewModel(context: Context, private val mealDao: MealDao): ViewModel() {
    val allMeals: Flow<List<MealWithFoodList>?> = mealDao.getAllMealEntries()

    companion object {
        private const val PROTEIN_KEY = "PROTEIN"
        private const val PROTEIN_DEFAULT = 100

        private const val CARBOHYDRATES_KEY = "CARBOHYDRATES"
        private const val CARBOHYDRATES_DEFAULT = 500

        private const val FATS_KEY = "FATS"
        private const val FATS_DEFAULT = 50
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("daily_goal", MODE_PRIVATE)

    private val _goalProtein = MutableStateFlow(sharedPreferences.getInt(PROTEIN_KEY, PROTEIN_DEFAULT))
    val goalProtein: StateFlow<Int> = _goalProtein

    private val _goalCarbohydrates = MutableStateFlow(sharedPreferences.getInt(CARBOHYDRATES_KEY, CARBOHYDRATES_DEFAULT))
    val goalCarbohydrates: StateFlow<Int> = _goalCarbohydrates

    private val _goalFats = MutableStateFlow(sharedPreferences.getInt(FATS_KEY, FATS_DEFAULT))
    val goalFats: StateFlow<Int> = _goalFats

    val goalCalories: StateFlow<Int> = combine(goalProtein, goalCarbohydrates, goalFats) { protein, carbs, fats ->
        (protein * 4) + (carbs * 4) + (fats * 9)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _goalProtein.value * 4 + _goalCarbohydrates.value * 4 + _goalFats.value * 9
    )

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            PROTEIN_KEY -> _goalProtein.update { sharedPreferences.getInt(PROTEIN_KEY, PROTEIN_DEFAULT) }
            CARBOHYDRATES_KEY -> _goalCarbohydrates.update { sharedPreferences.getInt(CARBOHYDRATES_KEY, CARBOHYDRATES_DEFAULT) }
            FATS_KEY -> _goalFats.update { sharedPreferences.getInt(FATS_KEY, FATS_DEFAULT) }
        }
    }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onCleared() {
        super.onCleared()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    fun updateProteinGoal(newProteinGoal: Int) {
        sharedPreferences
            .edit()
            .putInt(PROTEIN_KEY, newProteinGoal)
            .apply()

        _goalProtein.value = newProteinGoal
    }

    fun updateCarbohydratesGoal(newCarbohydratesGoal: Int) {
        sharedPreferences
            .edit()
            .putInt(CARBOHYDRATES_KEY, newCarbohydratesGoal)
            .apply()

        _goalCarbohydrates.value = newCarbohydratesGoal
    }

    fun updateFatsGoal(newFatsGoal: Int) {
        sharedPreferences
            .edit()
            .putInt(FATS_KEY, newFatsGoal)
            .apply()

        _goalFats.value = newFatsGoal
    }

    suspend fun getTodaysMealEntries(): Flow<List<MealWithFoodList>?> {
        return withContext(Dispatchers.IO) {
            mealDao.getTodaysMealEntries(today = LocalDate.now())
        }
    }

    suspend fun deleteMeal(mealId: Long) {
        withContext(Dispatchers.IO) {
            mealDao.deleteMeal(mealId)
        }
    }

    suspend fun getWeeklyMealEntries(): Flow<List<MealWithFoodList>?> {
        return withContext(Dispatchers.IO) {
            mealDao.getThisWeeksMealEntries(
                firstDayOfWeek = LocalDate.now().minusDays(LocalDate.now().dayOfWeek.value.toLong() - 1),
                today = LocalDate.now()
            )
        }
    }
}