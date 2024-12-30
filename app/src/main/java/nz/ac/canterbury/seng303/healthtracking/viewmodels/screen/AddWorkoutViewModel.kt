package nz.ac.canterbury.seng303.healthtracking.viewmodels.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import nz.ac.canterbury.seng303.healthtracking.viewmodels.database.WorkoutViewModel

class AddWorkoutViewModel(
    workoutViewModel: WorkoutViewModel
) : ViewModel() {
    private val _exercises = mutableStateListOf<String>()
    val exercises: List<String> get() = _exercises

    fun addExercise(name: String) {
        _exercises.add(name)
    }}