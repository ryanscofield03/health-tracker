package com.healthtracking.app.viewmodels.database

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.healthtracking.app.daos.MealDao
import com.healthtracking.app.entities.MealWithFoodList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MealViewModel(context: Context, private val mealDao: MealDao): ViewModel() {
    val allMeals: Flow<List<MealWithFoodList>?> = mealDao.getAllMealEntries()

    companion object {
        private const val PROTEIN_KEY = "PROTEIN"
        private const val PROTEIN_DEFAULT = 100

        private const val CARBOHYDRATES_KEY = "CARBOHYDRATES"
        private const val CARBOHYDRATES_DEFAULT = 500

        private const val FATS_KEY = "FATS"
        private const val FATS_DEFAULT = 60
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("daily_goal", MODE_PRIVATE)
    val goalCalories get() = goalProtein*4 + goalCarbohydrates*4 + goalFats*9

    private val _goalProtein = MutableStateFlow(sharedPreferences.getInt(PROTEIN_KEY, PROTEIN_DEFAULT))
    val goalProtein: Int get() = _goalProtein.value

    private val _goalCarbohydrates = MutableStateFlow(sharedPreferences.getInt(CARBOHYDRATES_KEY, CARBOHYDRATES_DEFAULT))
    val goalCarbohydrates: Int get() = _goalCarbohydrates.value

    private val _goalFats = MutableStateFlow(sharedPreferences.getInt(FATS_KEY, FATS_DEFAULT))
    val goalFats: Int get() = _goalFats.value

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            when (key) {
                PROTEIN_KEY -> _goalProtein.update { sharedPreferences.getInt(PROTEIN_KEY, PROTEIN_DEFAULT) }
                CARBOHYDRATES_KEY -> _goalCarbohydrates.update { sharedPreferences.getInt(CARBOHYDRATES_KEY, CARBOHYDRATES_DEFAULT) }
                FATS_KEY -> _goalFats.update { sharedPreferences.getInt(FATS_KEY, FATS_DEFAULT) }
            }
        }
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

    override fun onCleared() {
        super.onCleared()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener { _, _ -> }
    }

    suspend fun getTodaysMealEntries(): Flow<List<MealWithFoodList>?> {
        return withContext(Dispatchers.IO) {
            val startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
            val endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX)

            mealDao.getTodaysMealEntries(startOfDay, endOfDay)
        }
    }

    suspend fun deleteMeal(mealId: Long) {
        withContext(Dispatchers.IO) {
            mealDao.deleteMeal(mealId)
        }
    }
}