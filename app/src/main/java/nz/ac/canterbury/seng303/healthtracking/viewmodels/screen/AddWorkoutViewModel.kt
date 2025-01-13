package nz.ac.canterbury.seng303.healthtracking.viewmodels.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.healthtracking.R
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.entities.Workout
import nz.ac.canterbury.seng303.healthtracking.viewmodels.database.ExerciseViewModel
import nz.ac.canterbury.seng303.healthtracking.viewmodels.database.WorkoutViewModel
import java.time.DayOfWeek

class AddWorkoutViewModel(
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

    private val _exercisesErrorMessageId = mutableStateOf<Int?>(null)
    val exercisesErrorMessageId: Int? get() = _exercisesErrorMessageId.value

    // initial values which are required when saving an updated workout
    private val _currentWorkoutId = mutableStateOf<Long?>(null)
    private val _currentExercises = mutableStateListOf<Exercise>()
    private val _loaded = mutableStateOf(false)

    fun addExercise(exercise: Exercise) {
        _exercises.add(exercise)
        validateExercises()
    }

    fun removeExercise(exercise: Exercise) {
        _exercises.remove(exercise)
        validateExercises()
    }

    fun clearExercises() {
        _exercises.clear()
    }

    fun updateName(updatedName: String) {
        _name.value = updatedName
        validateName()
    }

    fun updateDescription(updatedDescription: String) {
        _description.value = updatedDescription
        validateDescription()
    }

    fun toggleScheduledDay(day: DayOfWeek) {
        if (scheduledDays.contains(day)) _scheduledDays.remove(day) else _scheduledDays.add(day)
    }

    private fun validateFields() {
        validateName()
        validateDescription()
        validateExercises()
    }

    private fun validateName() {
        _nameErrorMessageId.value =
            if (_name.value.isBlank())
                R.string.name_error_message
            else null
    }

    private fun validateDescription() {
        _descriptionErrorMessageId.value =
            if (_description.value.isBlank())
                R.string.description_error_message
            else null
    }

    private fun validateExercises() {
        _exercisesErrorMessageId.value =
            if (_exercises.isEmpty())
                R.string.exercise_error_message
            else null
    }

    fun save() {
        val workout = Workout(
            id = _currentWorkoutId.value ?: 0,
            name = name,
            description = description,
            schedule = scheduledDays
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

    fun isValid(): Boolean {
        validateFields()
        return name.isNotBlank() && description.isNotBlank() && _exercises.isNotEmpty()
    }

    fun clear() {
        _exercises.clear()
        _name.value = ""
        _description.value = ""
        _scheduledDays.clear()

        _nameErrorMessageId.value = null
        _descriptionErrorMessageId.value = null
        _exercisesErrorMessageId.value = null

        _currentWorkoutId.value = null
        _currentExercises.clear()
        _loaded.value = false
    }

    fun addWorkoutInfo(workout: Workout) {
        if (!_loaded.value) {
            _loaded.value = true
            updateName(workout.name)
            updateDescription(workout.description)
            workout.schedule.forEach { toggleScheduledDay(it) }
            loadExercisesForWorkout(workout.id)

            _currentWorkoutId.value = workout.id
        }
    }

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