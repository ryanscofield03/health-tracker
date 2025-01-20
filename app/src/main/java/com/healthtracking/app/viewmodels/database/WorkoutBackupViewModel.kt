package com.healthtracking.app.viewmodels.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.daos.WorkoutBackupDao
import com.healthtracking.app.entities.WorkoutBackup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class WorkoutBackupViewModel(
    private val workoutBackupDao: WorkoutBackupDao
) : ViewModel() {
    fun addWorkoutBackup(workoutId: Long,
                         exerciseIndex: Int,
                         entries: List<List<Pair<Int, Int>>>,
                         timerStart: LocalDateTime) {
        viewModelScope.launch {
            val workoutBackup = WorkoutBackup(
                id = workoutId,
                exerciseIndex = exerciseIndex,
                entries = entries,
                timerStart = timerStart
            )
            workoutBackupDao.upsertWorkoutBackup(workoutBackup)
        }
    }

    fun deleteWorkoutBackup(workoutId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutBackupDao.deleteWorkoutBackup(workoutId)
        }
    }

    suspend fun getWorkoutBackup(workoutId: Long): WorkoutBackup? {
        return withContext(Dispatchers.IO) {
            workoutBackupDao.getWorkoutBackup(workoutId)
        }
    }
}