package nz.ac.canterbury.seng303.healthtracking.viewmodels.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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

    private var _name = mutableStateOf("")
    val name: String get() = _name.value

    private var _description = mutableStateOf("")
    val description: String get() = _description.value

    val scheduledDays = mutableStateListOf<DayOfWeek>()

    fun addExercise(exercise: Exercise) {
        _exercises.add(exercise)
    }

    fun updateName(updatedName: String) {
        _name.value = updatedName
    }

    fun updateDescription(updatedDescription: String) {
        _description.value = updatedDescription
    }

    fun toggleScheduledDay(day: DayOfWeek) {
        if (scheduledDays.contains(day)) scheduledDays.remove(day) else scheduledDays.add(day)
    }

    fun save() {
        val workout = Workout(name = name, description = description, schedule = scheduledDays)
        workoutViewModel.addWorkout(workout)

        exercises.forEach { exercise ->
            exerciseViewModel.addExercise(exercise)
            workoutViewModel.addExerciseToWorkout(workoutId = workout.id, exerciseId = exercise.id)
        }
    }
}