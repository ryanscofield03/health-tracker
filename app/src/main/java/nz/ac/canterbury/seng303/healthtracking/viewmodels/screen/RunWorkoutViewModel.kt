package nz.ac.canterbury.seng303.healthtracking.viewmodels.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.entities.Workout

class RunWorkoutViewModel(
    workout: Workout,
    exercises: List<Exercise>
) : ViewModel() {
    val workoutName = workout.name

    private val _currentExercise = mutableStateOf(
        if (exercises.isNotEmpty()) exercises[0] else Exercise(name = "Not Found")
    )
    val currentExercise get() = _currentExercise.value

    private val _currentExerciseEntry = mutableStateListOf<Pair<Int, Int>>(Pair(0, 0))
    val currentExerciseEntry get() = _currentExerciseEntry.toList()
}