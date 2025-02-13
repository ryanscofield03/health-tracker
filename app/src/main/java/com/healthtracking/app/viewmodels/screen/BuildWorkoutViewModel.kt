package com.healthtracking.app.viewmodels.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.R
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.Workout
import com.healthtracking.app.viewmodels.database.ExerciseViewModel
import com.healthtracking.app.viewmodels.database.WorkoutViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class BuildWorkoutViewModel(
    private val workoutViewModel: WorkoutViewModel,
    private val exerciseViewModel: ExerciseViewModel
) : ViewModel() {
    private val _exercises = mutableStateListOf<Exercise>()
    val exercises: List<Exercise> get() = _exercises

    private val _name = mutableStateOf("")
    val name: String get() = _name.value

    private val _description = mutableStateOf("")
    val description: String get() = _description.value

    private val _scheduledDays = mutableStateListOf<DayOfWeek>()
    val scheduledDays: List<DayOfWeek> get() = _scheduledDays

    private val _nameErrorMessageId = mutableStateOf<Int?>(null)
    val nameErrorMessageId: Int? get() = _nameErrorMessageId.value

    private val _descriptionErrorMessageId = mutableStateOf<Int?>(null)
    val descriptionErrorMessageId: Int? get() = _descriptionErrorMessageId.value

    private val _exerciseSearch = mutableStateOf("")
    val exerciseSearch: String get() = _exerciseSearch.value

    private val _exerciseAddHasError = MutableStateFlow(false)
    val exerciseAddHasExercise: StateFlow<Boolean> = _exerciseAddHasError

    // initial values which are required when saving an updated workout
    private val _currentWorkoutId = mutableStateOf<Long?>(null)
    private val _currentExercises = mutableStateListOf<Exercise>()
    private val _loaded = mutableStateOf(false)

    /**
     * Adds exercise to the list of exercises
     */
    fun addExercise(exercise: Exercise) {
        _exercises.add(exercise)
    }

    fun updateExerciseAddToError(hasError: Boolean) {
        _exerciseAddHasError.value = hasError
    }

    /**
     * Removes exercise from the list of exercises
     */
    fun removeExercise(exercise: Exercise) {
        _exercises.remove(exercise)
    }

    /**
     * Clears the list of exercises
     */
    fun clearExercises() {
        _exercises.clear()
    }

    /**
     * Updates the name of the workout
     */
    fun updateName(updatedName: String) {
        _name.value = updatedName
        validateName()
    }

    /**
     * Updates the description of the workout
     */
    fun updateDescription(updatedDescription: String) {
        _description.value = updatedDescription
        validateDescription()
    }

    /**
     * Updates the search on the exercise screen
     */
    fun updateExerciseSearch(updatedExerciseSearch: String) {
        _exerciseSearch.value = updatedExerciseSearch

        if (updatedExerciseSearch.isNotBlank()) {
            updateExerciseAddToError(false)
        }
    }

    /**
     * Toggles a scheduled day on or off
     */
    fun toggleScheduledDay(day: DayOfWeek) {
        if (scheduledDays.contains(day)) _scheduledDays.remove(day) else _scheduledDays.add(day)
    }

    /**
     * Validates the workout name and description fields (e.g. updates the error messages & isError))
     */
    private fun validateFields() {
        validateName()
        validateDescription()
    }

    /**
     * Validates the workout name field
     */
    private fun validateName() {
        _nameErrorMessageId.value =
            if (_name.value.isBlank())
                R.string.name_error_message
            else null
    }

    /**
     * Validates the workout description field
     */
    private fun validateDescription() {
        _descriptionErrorMessageId.value =
            if (_description.value.isBlank())
                R.string.description_error_message
            else null
    }

    /**
     * Validates the exercise search field
     */
    fun validExerciseSearch(): Boolean {
        return exerciseSearch.isNotBlank()
    }

    /**
     * Saves the workout
     */
    fun save() {
        val workout = Workout(
            id = _currentWorkoutId.value ?: 0,
            name = name,
            description = description,
            schedule = scheduledDays.map { Pair(it, LocalDate.now()) }
        )

        viewModelScope.launch {
            workoutViewModel.addWorkoutSuspendCoroutineWrapper(workout).let { workoutId ->
                if (_currentWorkoutId.value == null) {
                    // Add all new exercises
                    exercises.forEach { exercise ->
                        val exerciseId = exerciseViewModel.addExerciseSuspendSuspendCoroutineWrapper(exercise)
                        exerciseViewModel.addExerciseToWorkoutSuspendCoroutineWrapper(workoutId, exerciseId)
                    }
                } else {
                    // Remove exercises that were previously saved but have been removed
                    _currentExercises.forEach { savedExercise ->
                        if (exercises.none { it.name == savedExercise.name }) {
                            exerciseViewModel.deleteExerciseSuspendSuspendCoroutineWrapper(savedExercise)
                        }
                    }

                    // Add new exercises that have been added to the workout
                    exercises.forEach { exercise ->
                        if (_currentExercises.none { it.name == exercise.name }) {
                            val exerciseId = exerciseViewModel.addExerciseSuspendSuspendCoroutineWrapper(exercise)
                            exerciseViewModel.addExerciseToWorkoutSuspendCoroutineWrapper(_currentWorkoutId.value!!, exerciseId)
                        }
                    }
                }

                // Clear data after all operations are complete
                clear()
            }
        }
    }

    /**
     * Validates the workout and returns true if it is valid
     */
    fun isValid(): Boolean {
        validateFields()
        return name.isNotBlank() && description.isNotBlank() && _exercises.isNotEmpty()
    }

    /**
     * Clears the entire viewmodel
     */
    fun clear() {
        _exercises.clear()
        _name.value = ""
        _description.value = ""
        _scheduledDays.clear()

        _nameErrorMessageId.value = null
        _descriptionErrorMessageId.value = null

        _currentWorkoutId.value = null
        _currentExercises.clear()
        _loaded.value = false

        clearExerciseScreen()
    }

    /**
     * Clears the exercise adding/editing screen
     */
    fun clearExerciseScreen() {
        _exerciseSearch.value = ""
    }

    /**
     * Populates viewmodel with workout data for editing
     */
    fun addWorkoutInfo(workout: Workout) {
        if (!_loaded.value) {
            _loaded.value = true
            updateName(workout.name)
            updateDescription(workout.description)
            workout.schedule.forEach { toggleScheduledDay(it.first) }
            loadExercisesForWorkout(workout.id)

            _currentWorkoutId.value = workout.id
        }
    }

    /**
     * Loads exercises for a workout
     */
    private fun loadExercisesForWorkout(workoutId: Long) {
        viewModelScope.launch {
            val exercises = workoutViewModel.getExercisesForWorkout(workoutId)
            exercises.forEach { exercise ->
                addExercise(exercise)
                // for later when comparing what is and isn't new
                _currentExercises.add(exercise)
            }
        }
    }
}