package com.healthtracking.app.viewmodels.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.R
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.ExerciseHistory
import com.healthtracking.app.entities.Workout
import com.healthtracking.app.entities.WorkoutBackup
import com.healthtracking.app.entities.WorkoutHistory
import com.healthtracking.app.viewmodels.database.ExerciseHistoryViewModel
import com.healthtracking.app.viewmodels.database.WorkoutBackupViewModel
import com.healthtracking.app.viewmodels.database.WorkoutHistoryViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.Duration as JavaDuration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max

/**
 * ViewModel for storing, handling, and persisting data pertaining to the RunWorkoutScreen
 */
class RunWorkoutViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val workout: Workout,
    private var exercises: List<Exercise>,
    private val exerciseHistoryViewModel: ExerciseHistoryViewModel,
    private val workoutBackupViewModel: WorkoutBackupViewModel,
    private val workoutHistoryViewModel: WorkoutHistoryViewModel
) : ViewModel() {
    // store constants for savesStateHandle keys
    companion object {
        const val CURRENT_EXERCISE_INDEX_KEY = "currentExerciseIndex"
        const val EXERCISE_ENTRIES_KEY = "exerciseEntries"
    }

    // name of the workout
    val workoutName = workout.name

    // historical data for the workout
    private val _exercisesHistory = MutableStateFlow<List<ExerciseHistory?>>(emptyList())
    val exerciseHistory: StateFlow<List<ExerciseHistory?>> get() = _exercisesHistory.asStateFlow()

    // user entries for the workout - user controlled
    private val _exerciseEntries = MutableStateFlow(
        value = savedStateHandle.get<List<MutableList<Pair<Float, Int>>>>(EXERCISE_ENTRIES_KEY)
            ?: exercises.map { mutableListOf() }
    )
    private val exerciseEntries: StateFlow<List<MutableList<Pair<Float, Int>>>> = _exerciseEntries.asStateFlow()

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

    // for displaying time since last entry
    private var job: Job? = null
    private val _timerStart: MutableState<LocalDateTime> = mutableStateOf(LocalDateTime.now())
    var timer: MutableState<JavaDuration> = mutableStateOf(Duration.ZERO)

    private var canBackup: Boolean = true

    init {
        canBackup = true
        loadExerciseHistories()
        loadBackup()
        startTimer()
    }

    /**
     * Loads historical data for exercises asynchronously.
     */
    private fun loadExerciseHistories() {
        viewModelScope.launch {
            val tempList = exercises.map { exercise ->
                exerciseHistoryViewModel.getMostRecentHistoryForExercise(exercise.id)
            }
            _exercisesHistory.value = tempList
        }
    }

    /**
     * Loads the last saved data that was persisted from this screen
     * (this is automatically backed up and is useful for recovering data).
     */
    private fun loadBackup() {
        viewModelScope.launch {
            val backedUpData: WorkoutBackup? =
                workoutBackupViewModel.getWorkoutBackup(workoutId = workout.id)

            if (backedUpData != null) {
                _exerciseEntries.value = List(_exerciseEntries.value.size) { index ->
                    backedUpData.entries[index].toMutableList()
                }

                _timerStart.value = backedUpData.timerStart
            }

        }
    }

    /**
     * Persists user data to be restored later if backup is needed
     */
    private fun saveBackup() {
        if (canBackup) {
            viewModelScope.launch {
                workoutBackupViewModel.addWorkoutBackup(
                    workoutId = workout.id,
                    exerciseIndex = 0,
                    entries = _exerciseEntries.value,
                    timerStart = _timerStart.value
                )
            }
        }
    }

    /**
     * Removes the backup (when viewmodel is saved or canceled)
     */
    private fun removeBackup() {
        viewModelScope.launch {
            workoutBackupViewModel.deleteWorkoutBackup(workoutId = workout.id)
        }
    }

    /**
     * Starts a job to increment timer by 1 every second
     */
    private fun startTimer() {
        if (job?.isActive == true) return

        job = viewModelScope.launch {
            while (isActive) {
                timer.value = JavaDuration.between(_timerStart.value, LocalDateTime.now())
                delay(1000L)
                saveBackup()
            }
        }
    }

    /**
     * Stops job that is incrementing timer - we no longer
     * need to run this in the background when the user is finished with their workout
     */
    private fun stopTimer() {
        job?.cancel()
    }

    /**
     * Reset the timer - useful for when the user makes an entry
     */
    private fun resetTimer() {
        _timerStart.value = LocalDateTime.now()
        timer.value = JavaDuration.ZERO
    }

    /**
     * User makes an exercise entry, this is saved to the current entries list (if valid)
     * and then added to the main entries list/table.
     * @return true if data saves, false if the data is not valid and does not save.
     */
    fun saveEntry(exerciseIndex: Int): Boolean {
        _newRepFieldCanError.value = true
        _newWeightFieldCanError.value = true

        if (newRepsIsValid() && newWeightIsValid()) {
            val pair = Pair(newWeight.toFloat(), newReps.toInt())

            if (_editingEntryIndex.value != null) {
                val updatedEntries = _exerciseEntries.value.map { it.toMutableList() }.toMutableList()
                updatedEntries[exerciseIndex][_editingEntryIndex.value!!] = pair
                _exerciseEntries.value = updatedEntries
            } else {
                val updatedEntries = _exerciseEntries.value.map { it.toMutableList() }.toMutableList()
                updatedEntries[exerciseIndex].add(pair)
                _exerciseEntries.value = updatedEntries

                // only reset timer when we add an entry, not update
                resetTimer()
            }

            savedStateHandle[EXERCISE_ENTRIES_KEY] = _exerciseEntries.value
            clearEntry()
            saveBackup()
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
    fun canEditEntry(exerciseIndex: Int, entryIndex: Int): Boolean {
        return exerciseEntries.value[exerciseIndex].size > entryIndex
    }

    fun updateEditingEntryIndex(exerciseIndex: Int, entryIndex: Int) {
        _editingEntryIndex.value = entryIndex
        updateNewWeight(_exerciseEntries
            .value[exerciseIndex][_editingEntryIndex.value!!].first.toString())
        updateNewReps(_exerciseEntries
            .value[exerciseIndex][_editingEntryIndex.value!!].second.toString())
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
        return !_newWeightFieldCanError.value || newWeight.toFloatOrNull() != null
    }

    /**
     * Checks to see if the user can go to the previous exercise (e.g. not at the start of
     * the exercise list)
     * @return true if user is not on first exercise
     */
    fun canGoPreviousExercise(exerciseIndex: Int): Boolean {
        return exerciseIndex > 0
    }

    /**
     * Checks to see if the user can go to the previous exercise (e.g. not at the end of
     * the exercise list)
     * @return true if user is not on last exercise
     */
    fun canGoNextExercise(exerciseIndex: Int): Boolean {
        return exerciseIndex < exercises.size - 1
    }

    /**
     * Attempts to go to the previous exercise.
     * This is not allowed if the user is at the start of the list.
     */
    fun goToPreviousExercise(exerciseIndex: Int) {
        if (canGoPreviousExercise(exerciseIndex)) {
            savedStateHandle[CURRENT_EXERCISE_INDEX_KEY] = exerciseIndex
        }
    }

    /**
     * Attempts to go to the next exercise.
     * This is not allowed if the user is at the end of the list.
     */
    fun goToNextExercise(exerciseIndex: Int) {
        if (canGoNextExercise(exerciseIndex)) {
            savedStateHandle[CURRENT_EXERCISE_INDEX_KEY] = exerciseIndex
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
        workoutHistoryViewModel.addWorkoutHistory(
            workoutHistory = WorkoutHistory(
                name = workoutName,
                date = LocalDate.now()
            )
        )

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
        resetTimer()
        stopTimer()

        savedStateHandle[EXERCISE_ENTRIES_KEY] = null
        savedStateHandle[CURRENT_EXERCISE_INDEX_KEY] = null

        _exercisesHistory.value = listOf()
        _exerciseEntries.value = exercises.map { mutableListOf() }

        canBackup = false
        removeBackup()
    }

    /**
     * Get the current exercise for a given index
     */
    fun getCurrentExercise(pageIndex: Int): Exercise {
        return exercises[pageIndex]
    }

    /**
     * get the current exercise history date for a given index
     */
    fun getCurrentExerciseHistoryDate(exerciseIndex: Int): String? {
        return exerciseHistory
            .value[exerciseIndex]
            ?.date
            ?.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
    }

    /**
     * Get the number of rows for a given exercise index
     */
    fun getNumberOfRows(exerciseIndex: Int): Int {
        return max(
            a = exerciseEntries.value[exerciseIndex].size + 1,
            b = exerciseHistory.value[exerciseIndex]?.data?.size ?: 0,
        )
    }

    /**
     * Get the exercise entries for a given exercise index
     */
    fun getExerciseEntries(exerciseIndex: Int): List<Pair<Float, Int>> {
        return exerciseEntries.value[exerciseIndex]
    }

    /**
     * Get the history entries for a given exercise index
     */
    fun getHistoryEntries(pageIndex: Int): List<Pair<Float, Int>?> {
        return exerciseHistory.value[pageIndex]?.data ?: listOf()
    }
}