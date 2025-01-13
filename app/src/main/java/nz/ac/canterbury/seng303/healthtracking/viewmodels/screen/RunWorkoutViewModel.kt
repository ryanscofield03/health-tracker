package nz.ac.canterbury.seng303.healthtracking.viewmodels.screen

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.entities.ExerciseHistory
import nz.ac.canterbury.seng303.healthtracking.entities.Workout
import nz.ac.canterbury.seng303.healthtracking.viewmodels.database.ExerciseHistoryViewModel
import java.time.LocalDate

class RunWorkoutViewModel(
    val workout: Workout,
    val exercises: List<Exercise>,
    val exerciseHistoryViewModel: ExerciseHistoryViewModel
) : ViewModel() {
    val workoutName = workout.name

    private val _currentExerciseIndex = mutableIntStateOf(0)

    val currentExercise: Exercise
        get() = exercises[_currentExerciseIndex.intValue]

    private val _exerciseEntries = mutableStateOf<List<List<Pair<Int, Int>>>>(exercises.map { listOf() })
    private val _currentExerciseEntries = mutableStateListOf<Pair<Int, Int>>()
    val currentExerciseEntries get() = _currentExerciseEntries.toList()

    private val _newWeight = mutableStateOf("")
    val newWeight get() = _newWeight.value

    private val _newReps = mutableStateOf("")
    val newReps get() = _newReps.value

    private val _newRepFieldCanError = mutableStateOf(false)
    private val _newWeightFieldCanError = mutableStateOf(false)

    private fun updateCurrentExerciseEntries() {
        _currentExerciseEntries.clear()
        _currentExerciseEntries.addAll(_exerciseEntries.value[_currentExerciseIndex.intValue])
    }

    private fun saveCurrentExerciseEntries() {
        val tempList = _exerciseEntries.value.toMutableList()
        tempList[_currentExerciseIndex.intValue] = currentExerciseEntries
        _exerciseEntries.value = tempList
    }

    fun updateNewWeight(updatedWeight: String) {
        _newWeightFieldCanError.value = true
        _newWeight.value = updatedWeight
    }

    fun updateNewReps(updatedReps: String) {
        _newRepFieldCanError.value = true
        _newReps.value = updatedReps
    }

    fun newRepsIsValid(): Boolean {
        return !_newRepFieldCanError.value || newReps.toIntOrNull() != null
    }

    fun newWeightIsValid(): Boolean {
        return !_newWeightFieldCanError.value || newWeight.toIntOrNull() != null
    }

    fun saveEntry(): Boolean {
        _newRepFieldCanError.value = true
        _newWeightFieldCanError.value = true

        if (newRepsIsValid() && newWeightIsValid()) {
            _currentExerciseEntries.add(Pair(newWeight.toInt(), newReps.toInt()))
            clearEntry()
            return true
        } else {
            return false
        }
    }

    fun clearEntry() {
        updateNewReps("")
        updateNewWeight("")
        _newWeightFieldCanError.value = false
        _newRepFieldCanError.value = false
    }

    fun canGoPreviousExercise(): Boolean {
        return _currentExerciseIndex.intValue > 0
    }

    fun canGoNextExercise(): Boolean {
        return _currentExerciseIndex.intValue < exercises.size - 1
    }

    fun goToPreviousExercise() {
        if (canGoPreviousExercise()) {
            saveCurrentExerciseEntries()
            _currentExerciseIndex.intValue -= 1
            updateCurrentExerciseEntries()
        }
    }

    fun goToNextExercise() {
        if (canGoNextExercise()) {
            saveCurrentExerciseEntries()
            _currentExerciseIndex.intValue += 1
            updateCurrentExerciseEntries()
        }
    }

    fun saveWorkoutHistory() {
        exercises.forEachIndexed { index, exercise ->
            val exerciseHistory = ExerciseHistory(
                date = LocalDate.now(),
                data = _exerciseEntries.value[index]
            )

            exerciseHistoryViewModel.addExerciseHistory(
                exerciseId = exercise.id,
                exerciseHistory = exerciseHistory
            )
        }
    }
}