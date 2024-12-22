package nz.ac.canterbury.seng303.healthtracking.viewmodels.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.healthtracking.daos.WorkoutDao
import nz.ac.canterbury.seng303.healthtracking.entities.Workout

class WorkoutViewModel(
    private val workoutDao: WorkoutDao,
) : ViewModel() {
    val allWorkouts: LiveData<List<Workout>> = workoutDao.getAllWorkouts()

    fun addWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutDao.upsertWorkout(workout)
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
}