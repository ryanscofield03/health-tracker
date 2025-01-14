package nz.ac.canterbury.seng303.healthtracking.viewmodels.screen

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.entities.ExerciseHistory
import nz.ac.canterbury.seng303.healthtracking.entities.Workout
import nz.ac.canterbury.seng303.healthtracking.viewmodels.database.ExerciseHistoryViewModel
import java.time.LocalDateTime

/**
 * ViewModel for storing, handling, and persisting data pertaining to the RunWorkoutScreen
 */
class RunWorkoutViewModel(
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
    val workout: Workout,
    var exercises: List<Exercise>,
    val exerciseHistoryViewModel: ExerciseHistoryViewModel
) : ViewModel() {
    // store constants for savesStateHandle keys
    companion object {
        const val CURRENT_EXERCISE_INDEX_KEY = "currentExerciseIndex"
        const val EXERCISE_ENTRIES_KEY = "exerciseEntries"
    }

    // name of the workout
    val workoutName = workout.name

    // current index of the workout - user controlled
    private val _currentExerciseIndex = mutableIntStateOf(savedStateHandle.get<Int>(CURRENT_EXERCISE_INDEX_KEY) ?: 0)

    // current exercise - based on index
    val currentExercise: Exercise
        get() = exercises[_currentExerciseIndex.intValue]

    // historical data for the workout
    private val _exercisesHistory = MutableStateFlow<List<ExerciseHistory?>>(emptyList())
    val currentExerciseHistory: ExerciseHistory? get() =
        _exercisesHistory.value.getOrNull(_currentExerciseIndex.intValue)

    // user entries for the workout - user controlled
    private val _exerciseEntries = mutableStateOf(
        savedStateHandle.get<List<MutableList<Pair<Int, Int>>>>(EXERCISE_ENTRIES_KEY)
            ?: exercises.map { mutableListOf() }
    )
    val currentExerciseEntries: List<Pair<Int, Int>>
        get() = _exerciseEntries.value[_currentExerciseIndex.intValue]

    // data for new entry
    private val _newWeight = mutableStateOf("")
    val newWeight get() = _newWeight.value

    // data for new entry
    private val _newReps = mutableStateOf("")
    val newReps get() = _newReps.value

    // data for new entry
    private val _newRepFieldCanError = mutableStateOf(false)
    private val _newWeightFieldCanError = mutableStateOf(false)

    // data for editing old entry
    private val _editingEntryIndex = mutableStateOf<Int?>(null)

    /**
     * Loads historical data for exercises asynchronously.
     */
    fun loadExerciseHistories() {
        viewModelScope.launch {
            val tempList = exercises.map { exercise ->
                exerciseHistoryViewModel.getMostRecentHistoryForExercise(exercise.id)
            }
            _exercisesHistory.value = tempList
        }
    }

    /**
     * User makes an exercise entry, this is saved to the current entries list (if valid)
     * and then added to the main entries list/table.
     * @return true if data saves, false if the data is not valid and does not save.
     */
    fun saveEntry(): Boolean {
        _newRepFieldCanError.value = true
        _newWeightFieldCanError.value = true

        if (newRepsIsValid() && newWeightIsValid()) {
            val pair = Pair(newWeight.toInt(), newReps.toInt())

            if (_editingEntryIndex.value != null) {
                val updatedEntries = _exerciseEntries.value.toMutableList()
                updatedEntries[_currentExerciseIndex.intValue][_editingEntryIndex.value!!] = pair
                _exerciseEntries.value = updatedEntries
            } else {
                val updatedEntries = _exerciseEntries.value.toMutableList()
                updatedEntries[_currentExerciseIndex.intValue].add(pair)
                _exerciseEntries.value = updatedEntries
            }

            savedStateHandle[EXERCISE_ENTRIES_KEY] = _exerciseEntries.value
            clearEntry()
            return true
        } else {
            return false
        }
    }

    /**
     * Clears entry - this is so that when the user opens the entry
     * form again, they open a blank form
     */
    fun clearEntry() {
        updateNewReps("")
        updateNewWeight("")
        _newWeightFieldCanError.value = false
        _newRepFieldCanError.value = false
        _editingEntryIndex.value = null
    }

    /**
     * Checks to see if given index of current exercise has an entry already
     */
    fun canEditEntry(index: Int): Boolean {
        return currentExerciseEntries.size > index
    }

    fun updateEditingEntryIndex(index: Int) {
        _editingEntryIndex.value = index
        updateNewWeight(_exerciseEntries
            .value[_currentExerciseIndex.intValue][_editingEntryIndex.value!!].first.toString())
        updateNewReps(_exerciseEntries
            .value[_currentExerciseIndex.intValue][_editingEntryIndex.value!!].second.toString())
    }

    fun updateNewWeight(updatedWeight: String) {
        _newWeightFieldCanError.value = true
        _newWeight.value = updatedWeight
    }

    fun updateNewReps(updatedReps: String) {
        _newRepFieldCanError.value = true
        _newReps.value = updatedReps
    }

    /**
     * Validates the current value of reps
     * @return true if it is not null and it can currently error
     */
    fun newRepsIsValid(): Boolean {
        return !_newRepFieldCanError.value || newReps.toIntOrNull() != null
    }

    /**
     * Validates the current value of weight
     * @return true if it is not null and it can currently error
     */
    fun newWeightIsValid(): Boolean {
        return !_newWeightFieldCanError.value || newWeight.toIntOrNull() != null
    }

    /**
     * Checks to see if the user can go to the previous exercise (e.g. not at the start of
     * the exercise list)
     * @return true if user is not on first exercise
     */
    fun canGoPreviousExercise(): Boolean {
        return _currentExerciseIndex.intValue > 0
    }

    /**
     * Checks to see if the user can go to the previous exercise (e.g. not at the end of
     * the exercise list)
     * @return true if user is not on last exercise
     */
    fun canGoNextExercise(): Boolean {
        return _currentExerciseIndex.intValue < exercises.size - 1
    }

    /**
     * Attempts to go to the previous exercise.
     * This is not allowed if the user is at the start of the list.
     */
    fun goToPreviousExercise() {
        if (canGoPreviousExercise()) {
            _currentExerciseIndex.intValue -= 1
            savedStateHandle[CURRENT_EXERCISE_INDEX_KEY] = _currentExerciseIndex.intValue
        }
    }

    /**
     * Attempts to go to the next exercise.
     * This is not allowed if the user is at the end of the list.
     */
    fun goToNextExercise() {
        if (canGoNextExercise()) {
            _currentExerciseIndex.intValue += 1
            savedStateHandle[CURRENT_EXERCISE_INDEX_KEY] = _currentExerciseIndex.intValue
        }
    }

    /**
     * Validate that each exercise has at least one entry
     */
    fun validateEntries(): Boolean {
        return _exerciseEntries.value.all { list -> list.isNotEmpty() }
    }

    /**
     * Saves workout data (exercise entries) to persistence
     */
    fun saveWorkoutHistory() {
        exercises.forEachIndexed { index, exercise ->
            val exerciseHistory = ExerciseHistory(
                date = LocalDateTime.now(),
                data = _exerciseEntries.value[index]
            )

            exerciseHistoryViewModel.addExerciseHistory(
                exerciseId = exercise.id,
                exerciseHistory = exerciseHistory
            )
        }

        clearViewModel()
    }

    /**
     * Remove all data from view model for next usage
     */
    fun clearViewModel() {
        savedStateHandle[EXERCISE_ENTRIES_KEY] = null
        savedStateHandle[CURRENT_EXERCISE_INDEX_KEY] = null

        _currentExerciseIndex.intValue = 0
        _exerciseEntries.value = exercises.map { mutableListOf() }
    }
}

