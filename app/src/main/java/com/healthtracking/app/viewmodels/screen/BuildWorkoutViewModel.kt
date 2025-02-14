package com.healthtracking.app.viewmodels.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.Metric
import com.healthtracking.app.entities.Workout
import com.healthtracking.app.viewmodels.database.ExerciseViewModel
import com.healthtracking.app.viewmodels.database.WorkoutViewModel
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

    private val _exerciseSearch = mutableStateOf("")
    val exerciseSearch: String get() = _exerciseSearch.value

    // initial values which are required when saving an updated workout
    private val _currentWorkoutId = mutableStateOf<Long?>(null)
    private val _currentExercises = mutableStateListOf<Exercise>()

    private val _alreadySaved: MutableState<Boolean> = mutableStateOf(false)
    val alreadySaved: Boolean get() = _alreadySaved.value

    private val _alreadyAddedExercise: MutableState<Boolean> = mutableStateOf(false)
    val alreadyAddedExercise: Boolean get() = _alreadyAddedExercise.value

    private val _editExerciseDialogIndex = mutableStateOf<Int?>(null)

    private val _editExerciseDialogName = mutableStateOf("")
    val editExerciseDialogName: String get() = _editExerciseDialogName.value

    private val _editExerciseDialogMetrics = mutableStateOf<List<Metric>>(listOf())
    val editExerciseDialogMetrics: List<Metric> get() = _editExerciseDialogMetrics.value

    /**
     * Adds exercise to the list of exercises
     */
    fun addExercise(exercise: Exercise) {
        _exercises.add(exercise)

        _alreadyAddedExercise.value = false
    }

    fun failedToAddExercise() {
        _alreadyAddedExercise.value = true
    }

    /**
     * Removes exercise from the list of exercises
     */
    fun removeExercise(exercise: Exercise) {
        _exercises.remove(exercise)
    }

    /**
     * Updates the name of the workout
     */
    fun updateName(updatedName: String) {
        _name.value = updatedName
    }

    /**
     * Updates the description of the workout
     */
    fun updateDescription(updatedDescription: String) {
        _description.value = updatedDescription
    }

    /**
     * Updates the search on the exercise screen
     */
    fun updateExerciseSearch(updatedExerciseSearch: String) {
        _exerciseSearch.value = updatedExerciseSearch

        _alreadyAddedExercise.value = false
    }

    /**
     * Toggles a scheduled day on or off
     */
    fun toggleScheduledDay(day: DayOfWeek) {
        if (scheduledDays.contains(day)) _scheduledDays.remove(day) else _scheduledDays.add(day)
    }

    /**
     * Saves the workout
     */
    fun save() {
        _alreadySaved.value = true

        if (isValid()) {
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
    }

    /**
     * Validates the workout and returns true if it is valid
     */
    fun isValid(): Boolean {
        return isNameValid() && isDescriptionValid() && isExerciseValid()
    }

    fun isNameValid(): Boolean {
        return name.isNotBlank()
    }

    fun isDescriptionValid(): Boolean {
        return description.isNotBlank()
    }

    private fun isExerciseValid(): Boolean {
        return _exercises.isNotEmpty()
    }

    fun isExerciseSearchValid(): Boolean {
        return exerciseSearch.isNotBlank()
    }

    fun clearIfEdited() {
        if (_currentWorkoutId.value != null) {
            clear()
        }
    }

    /**
     * Clears the entire viewmodel
     */
    fun clear() {
        _exercises.clear()
        _name.value = ""
        _description.value = ""
        _scheduledDays.clear()

        _currentWorkoutId.value = null
        _currentExercises.clear()

        _alreadySaved.value = false

        clearExerciseScreen()
    }

    /**
     * Clears the exercise adding/editing screen
     */
    fun clearExerciseScreen() {
        _exerciseSearch.value = ""
        _alreadyAddedExercise.value = false
    }

    /**
     * Populates viewmodel with workout data for editing
     */
    fun addWorkoutInfo(workout: Workout) {
        updateName(workout.name)
        updateDescription(workout.description)
        workout.schedule.forEach { toggleScheduledDay(it.first) }
        loadExercisesForWorkout(workout.id)

        _currentWorkoutId.value = workout.id
    }

    fun populateEditDialog(exercise: Exercise) {
        _editExerciseDialogIndex.value = _exercises.indexOf(exercise)
        _editExerciseDialogName.value = exercise.name
        _editExerciseDialogMetrics.value = exercise.metrics
    }

    fun updateDialogMetrics(metrics: List<Metric>) {
        _editExerciseDialogMetrics.value = metrics
    }

    fun saveEditExerciseDialog() {
        if (_editExerciseDialogIndex.value == null) return
        if (_editExerciseDialogIndex.value!! >= _exercises.size) return

        _exercises[_editExerciseDialogIndex.value!!] = Exercise(
            name = _editExerciseDialogName.value,
            metrics = _editExerciseDialogMetrics.value
        )

        clearEditExerciseDialog()
    }

    fun clearEditExerciseDialog() {
        _editExerciseDialogIndex.value = null
        _editExerciseDialogName.value = ""
        _editExerciseDialogMetrics.value = listOf()
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