package nz.ac.canterbury.seng303.healthtracking.viewmodels.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nz.ac.canterbury.seng303.healthtracking.daos.WorkoutDao
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.entities.Workout
import nz.ac.canterbury.seng303.healthtracking.entities.WorkoutExerciseCrossRef

class WorkoutViewModel(
    private val workoutDao: WorkoutDao,
) : ViewModel() {
    val allWorkouts: LiveData<List<Workout>> = workoutDao.getAllWorkouts()

    fun addWorkout(workout: Workout, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val workoutId = workoutDao.upsertWorkout(workout)
            onResult(workoutId)
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutDao.deleteWorkout(workout)
        }
    }

    fun editWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutDao.upsertWorkout(workout)
        }
    }

    fun removeExerciseFromWorkout(workoutId: Long, exerciseId: Long) {
        viewModelScope.launch {
            workoutDao.deleteWorkoutExerciseCrossRef(workoutId, exerciseId)
        }
    }

    fun addExerciseToWorkout(workoutId: Long, exerciseId: Long) {
        viewModelScope.launch {
            val crossRef = WorkoutExerciseCrossRef(workoutId, exerciseId)
            workoutDao.upsertWorkoutExerciseCrossRef(crossRef)
        }
    }

    suspend fun getExercisesForWorkout(workoutId: Long): List<Exercise> {
        return withContext(Dispatchers.IO) {
            workoutDao.getExercisesForWorkout(workoutId)
        }
    }
}