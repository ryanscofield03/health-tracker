package nz.ac.canterbury.seng303.healthtracking.viewmodels.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nz.ac.canterbury.seng303.healthtracking.daos.ExerciseHistoryDao
import nz.ac.canterbury.seng303.healthtracking.entities.ExerciseHistory
import nz.ac.canterbury.seng303.healthtracking.entities.ExerciseHistoryCrossRef

class ExerciseHistoryViewModel(
    private val workoutHistoryDao: ExerciseHistoryDao
) : ViewModel() {
    fun addExerciseHistory(exerciseId: Long, exerciseHistory: ExerciseHistory) {
        viewModelScope.launch {
            val workoutHistoryId = workoutHistoryDao.upsertWorkoutHistory(exerciseHistory)
            addWorkoutHistoryToWorkout(workoutId = exerciseId, workoutHistoryId = workoutHistoryId)
        }
    }

    private fun addWorkoutHistoryToWorkout(workoutId: Long, workoutHistoryId: Long) {
        viewModelScope.launch {
            val crossRef = ExerciseHistoryCrossRef(workoutId, workoutHistoryId)
            workoutHistoryDao.upsertWorkoutHistoryCrossRef(crossRef)
        }
    }

    suspend fun getMostRecentHistoryForExercise(exerciseId: Long): ExerciseHistory {
        return withContext(Dispatchers.IO) {
            workoutHistoryDao.getMostRecentHistoryForExercise(exerciseId)
        }
    }
}