package com.healthtracking.app.viewmodels.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.healthtracking.app.daos.WorkoutDao
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.Workout
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WorkoutViewModel(
    private val workoutDao: WorkoutDao,
) : ViewModel() {
    val allWorkouts: StateFlow<List<Workout>?> = workoutDao
        .getAllWorkouts().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private fun addWorkout(workout: Workout, onResult: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val workoutId = workoutDao.upsertWorkout(workout)
            onResult(workoutId)
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                workoutDao.deleteWorkoutAndAssociatedData(
                    workout = workout
                )
            }
        }
    }

    fun editWorkout(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutDao.upsertWorkout(workout)
        }
    }

    fun removeExerciseFromWorkout(workoutId: Long, exerciseId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutDao.deleteWorkoutExerciseCrossRef(workoutId, exerciseId)
        }
    }

    suspend fun getExercisesForWorkout(workoutId: Long): List<Exercise> {
        return withContext(Dispatchers.IO) {
            workoutDao.getExercisesForWorkout(workoutId)
        }
    }

    suspend fun addWorkoutSuspendCoroutineWrapper(workout: Workout): Long =
        suspendCoroutine { continuation ->
            addWorkout(workout) { workoutId ->
                continuation.resume(workoutId)
            }
        }
}