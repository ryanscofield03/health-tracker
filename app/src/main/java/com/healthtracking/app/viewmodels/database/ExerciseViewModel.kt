package com.healthtracking.app.viewmodels.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.healthtracking.app.daos.ExerciseDao
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.WorkoutExerciseCrossRef
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ExerciseViewModel(
    private val exerciseDao: ExerciseDao
) : ViewModel() {
    val allExercises: LiveData<List<Exercise>> = exerciseDao.getAllExercises()

    fun addExercise(exercise: Exercise, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            var savedExercise: Exercise? = null
            withContext(Dispatchers.IO) {
                savedExercise = exerciseDao.getExerciseWithName(exercise.name)
            }

            if (savedExercise == null) {
                val exerciseId = exerciseDao.upsertExercise(exercise)
                onResult(exerciseId)
            } else {
                onResult(savedExercise!!.id)
            }

        }
    }

    fun addExerciseToWorkout(workoutId: Long, exerciseId: Long) {
        viewModelScope.launch {
            val crossRef = WorkoutExerciseCrossRef(workoutId, exerciseId)
            exerciseDao.upsertWorkoutExerciseCrossRef(crossRef)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseDao.deleteExercise(exercise)
        }
    }

    fun editExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseDao.upsertExercise(exercise)
        }
    }

    suspend fun addExerciseSuspendSuspendCoroutineWrapper(exercise: Exercise): Long =
        suspendCoroutine { continuation ->
            addExercise(exercise) { exerciseId ->
                continuation.resume(exerciseId)
            }
        }

    suspend fun deleteExerciseSuspendSuspendCoroutineWrapper(exercise: Exercise) =
        suspendCoroutine { continuation ->
            deleteExercise(exercise)
            continuation.resume(Unit)
        }

    suspend fun addExerciseToWorkoutSuspendCoroutineWrapper(workoutId: Long, exerciseId: Long) =
        suspendCoroutine { continuation ->
            addExerciseToWorkout(workoutId, exerciseId)
            continuation.resume(Unit)
        }
}