package nz.ac.canterbury.seng303.healthtracking.viewmodels.screen

import android.content.Context
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        viewModelScope.launch {
            workoutViewModel.addWorkout(
                Workout(name = name, description = description, schedule = scheduledDays),
                onResult = { workoutId ->
                    exercises.forEach { exercise ->
                        exerciseViewModel.addExercise(exercise, onResult = { exerciseId ->
                            workoutViewModel.addExerciseToWorkout(workoutId = workoutId, exerciseId = exerciseId)
                        })
                    }

                    // important to clear here after all exercises have been added
                    clear()
                }
            )
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
    }

    fun addWorkoutInfo(workout: Workout) {
        updateName(workout.name)
        updateDescription(workout.description)

        /* HANDLE SCHEDULED DAYS */

        loadExercisesForWorkout(workout.id)
    }

    private fun loadExercisesForWorkout(workoutId: Long) {
        viewModelScope.launch {
            val exercises = workoutViewModel.getExercisesForWorkout(workoutId)
            exercises.forEach { exercise ->
                addExercise(exercise)
            }
        }
    }
}