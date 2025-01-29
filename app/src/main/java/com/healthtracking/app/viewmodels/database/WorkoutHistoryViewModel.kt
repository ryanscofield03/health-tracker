package com.healthtracking.app.viewmodels.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.daos.WorkoutHistoryDao
import com.healthtracking.app.entities.WorkoutHistory
import kotlinx.coroutines.launch

class WorkoutHistoryViewModel(
    private val workoutHistoryDao: WorkoutHistoryDao
): ViewModel() {

    fun addWorkoutHistory(workoutHistory: WorkoutHistory) {
        viewModelScope.launch {
            workoutHistoryDao.upsertWorkoutHistory(workoutHistory)
        }
    }
}