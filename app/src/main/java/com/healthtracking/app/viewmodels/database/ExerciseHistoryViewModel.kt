package com.healthtracking.app.viewmodels.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.healthtracking.app.daos.ExerciseHistoryDao
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.ExerciseHistory
import com.healthtracking.app.entities.ExerciseHistoryCrossRef
import kotlinx.coroutines.flow.Flow

class ExerciseHistoryViewModel(
    private val workoutHistoryDao: ExerciseHistoryDao
) : ViewModel() {
    suspend fun getExerciseHistory(exerciseName: String): Flow<List<ExerciseHistory>?> {
        return withContext(Dispatchers.IO) {
            workoutHistoryDao.getHistoryForExerciseName(exerciseName = exerciseName)
        }
    }

    fun addExerciseHistory(exerciseId: Long, exerciseHistory: ExerciseHistory) {
        viewModelScope.launch(Dispatchers.IO) {
            val workoutHistoryId = workoutHistoryDao.upsertWorkoutHistory(exerciseHistory)
            addWorkoutHistoryToWorkout(workoutId = exerciseId, workoutHistoryId = workoutHistoryId)
        }
    }

    private fun addWorkoutHistoryToWorkout(workoutId: Long, workoutHistoryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val crossRef = ExerciseHistoryCrossRef(workoutId, workoutHistoryId)
            workoutHistoryDao.upsertWorkoutHistoryCrossRef(crossRef)
        }
    }

    suspend fun getMostRecentHistoryForExercise(exerciseId: Long): ExerciseHistory? {
        return withContext(Dispatchers.IO) {
            workoutHistoryDao.getMostRecentHistoryForExercise(exerciseId)
        }
    }
}