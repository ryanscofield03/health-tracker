package nz.ac.canterbury.seng303.healthtracking.viewmodels.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nz.ac.canterbury.seng303.healthtracking.daos.WorkoutHistoryDao
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.entities.WorkoutHistory
import nz.ac.canterbury.seng303.healthtracking.entities.WorkoutHistoryCrossRef

class WorkoutHistoryViewModel(
    private val workoutHistoryDao: WorkoutHistoryDao
) : ViewModel() {
    fun addWorkoutHistory(workoutId: Long, workoutHistory: WorkoutHistory) {
        viewModelScope.launch {
            val workoutHistoryId = workoutHistoryDao.upsertWorkoutHistory(workoutHistory)
            addWorkoutHistoryToWorkout(workoutId = workoutId, workoutHistoryId = workoutHistoryId)
        }
    }

    private fun addWorkoutHistoryToWorkout(workoutId: Long, workoutHistoryId: Long) {
        viewModelScope.launch {
            val crossRef = WorkoutHistoryCrossRef(workoutId, workoutHistoryId)
            workoutHistoryDao.upsertWorkoutHistoryCrossRef(crossRef)
        }
    }

    suspend fun getMostRecentHistoryForWorkout(workoutId: Long) {
        return withContext(Dispatchers.IO) {
            workoutHistoryDao.getMostRecentHistoryForWorkout(workoutId)
        }
    }
}