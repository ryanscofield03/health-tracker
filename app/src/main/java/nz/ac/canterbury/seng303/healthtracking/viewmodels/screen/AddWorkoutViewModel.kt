package nz.ac.canterbury.seng303.healthtracking.viewmodels.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import nz.ac.canterbury.seng303.healthtracking.viewmodels.database.WorkoutViewModel
import java.time.DayOfWeek

class AddWorkoutViewModel(
    workoutViewModel: WorkoutViewModel
) : ViewModel() {
    private val _exercises = mutableStateListOf<String>()
    val exercises: List<String> get() = _exercises

    private var _name = mutableStateOf("")
    val name: String get() = _name.value

    private var _description = mutableStateOf("")
    val description: String get() = _description.value

    val scheduledDays = mutableStateListOf<DayOfWeek>()

    fun addExercise(name: String) {
        _exercises.add(name)
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
}