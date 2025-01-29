package com.healthtracking.app.viewmodels.screen

import androidx.lifecycle.ViewModel
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.WorkoutHistory
import com.healthtracking.app.viewmodels.database.ExerciseHistoryViewModel
import com.healthtracking.app.viewmodels.database.ExerciseViewModel
import com.healthtracking.app.viewmodels.database.MealViewModel
import com.healthtracking.app.viewmodels.database.SleepViewModel
import com.healthtracking.app.viewmodels.database.WorkoutHistoryViewModel
import com.healthtracking.app.viewmodels.database.WorkoutViewModel
import java.time.LocalDate

class StatsScreenViewModel(
    val workoutViewModel: WorkoutViewModel,
    val exerciseViewModel: ExerciseViewModel,
    val workoutHistoryViewModel: WorkoutHistoryViewModel,
    val exerciseHistoryViewModel: ExerciseHistoryViewModel,
    val mealViewModel: MealViewModel,
    val sleepViewModel: SleepViewModel
): ViewModel() {
    fun getHoursSleptData(): Map<LocalDate, Double> {
        return mapOf(
            Pair(LocalDate.now().minusDays(13), 7.5),
            Pair(LocalDate.now().minusDays(12), 6.0),
            Pair(LocalDate.now().minusDays(11), 7.5),
            Pair(LocalDate.now().minusDays(10), 8.0),
            Pair(LocalDate.now().minusDays(9), 7.5),
            Pair(LocalDate.now().minusDays(8), 6.0),
            Pair(LocalDate.now().minusDays(7), 7.5),
            Pair(LocalDate.now().minusDays(6), 8.0),
            Pair(LocalDate.now().minusDays(5), 6.0),
            Pair(LocalDate.now().minusDays(4), 7.0),
            Pair(LocalDate.now().minusDays(3), 6.5),
            Pair(LocalDate.now().minusDays(2), 8.0),
            Pair(LocalDate.now().minusDays(1), 7.5),
            Pair(LocalDate.now(), 8.0)
        )
    }

    fun getSleepRatingsData(): Map<LocalDate, Double> {
        return mapOf(
            Pair(LocalDate.now().minusDays(9), 1.0),
            Pair(LocalDate.now().minusDays(8), 2.0),
            Pair(LocalDate.now().minusDays(7), 3.0),
            Pair(LocalDate.now().minusDays(6), 4.0),
            Pair(LocalDate.now().minusDays(5), 2.0),
            Pair(LocalDate.now().minusDays(4), 5.0),
            Pair(LocalDate.now().minusDays(3), 4.0),
            Pair(LocalDate.now().minusDays(2), 3.0),
            Pair(LocalDate.now().minusDays(1), 2.0),
            Pair(LocalDate.now(), 3.0)
        )
    }

    fun getCaloriesData(): Map<LocalDate, Double> {
        return mapOf(
            Pair(LocalDate.now().minusDays(13), 2300.0),
            Pair(LocalDate.now().minusDays(12), 2700.0),
            Pair(LocalDate.now().minusDays(11), 2500.0),
            Pair(LocalDate.now().minusDays(10), 2300.0),
            Pair(LocalDate.now().minusDays(9), 2350.0),
            Pair(LocalDate.now().minusDays(8), 2550.0),
            Pair(LocalDate.now().minusDays(7), 2400.0),
            Pair(LocalDate.now().minusDays(6), 2600.0),
            Pair(LocalDate.now().minusDays(5), 2200.0),
            Pair(LocalDate.now().minusDays(4), 2600.0),
            Pair(LocalDate.now().minusDays(3), 2250.0),
            Pair(LocalDate.now().minusDays(2), 2550.0),
            Pair(LocalDate.now().minusDays(1), 2450.0),
            Pair(LocalDate.now(), 2300.0)
        )
    }

    fun getProteinData(): Map<LocalDate, Double> {
        return mapOf(
            Pair(LocalDate.now().minusDays(13), 75.0),
            Pair(LocalDate.now().minusDays(12), 120.0),
            Pair(LocalDate.now().minusDays(11), 75.0),
            Pair(LocalDate.now().minusDays(10), 110.0),
            Pair(LocalDate.now().minusDays(9), 95.0),
            Pair(LocalDate.now().minusDays(8), 70.0),
            Pair(LocalDate.now().minusDays(7), 75.0),
            Pair(LocalDate.now().minusDays(6), 80.0),
            Pair(LocalDate.now().minusDays(5), 65.0),
            Pair(LocalDate.now().minusDays(4), 100.0),
            Pair(LocalDate.now().minusDays(3), 85.0),
            Pair(LocalDate.now().minusDays(2), 80.0),
            Pair(LocalDate.now().minusDays(1), 95.0),
            Pair(LocalDate.now(), 100.0)
        )
    }

    fun getCarbsData(): Map<LocalDate, Double> {
        return mapOf(
            Pair(LocalDate.now().minusDays(13), 500.0),
            Pair(LocalDate.now().minusDays(12), 350.0),
            Pair(LocalDate.now().minusDays(11), 300.0),
            Pair(LocalDate.now().minusDays(10), 350.0),
            Pair(LocalDate.now().minusDays(9), 500.0),
            Pair(LocalDate.now().minusDays(8), 200.0),
            Pair(LocalDate.now().minusDays(7), 350.0),
            Pair(LocalDate.now().minusDays(6), 300.0),
            Pair(LocalDate.now().minusDays(5), 400.0),
            Pair(LocalDate.now().minusDays(4), 250.0),
            Pair(LocalDate.now().minusDays(3), 500.0),
            Pair(LocalDate.now().minusDays(2), 350.0),
            Pair(LocalDate.now().minusDays(1), 400.0),
            Pair(LocalDate.now(), 350.0)
        )
    }

    fun getFatsData(): Map<LocalDate, Double> {
        return mapOf(
            Pair(LocalDate.now().minusDays(13), 30.0),
            Pair(LocalDate.now().minusDays(12), 40.0),
            Pair(LocalDate.now().minusDays(11), 30.0),
            Pair(LocalDate.now().minusDays(10), 20.0),
            Pair(LocalDate.now().minusDays(9), 50.0),
            Pair(LocalDate.now().minusDays(8), 40.0),
            Pair(LocalDate.now().minusDays(7), 30.0),
            Pair(LocalDate.now().minusDays(6), 40.0),
            Pair(LocalDate.now().minusDays(5), 45.0),
            Pair(LocalDate.now().minusDays(4), 35.0),
            Pair(LocalDate.now().minusDays(3), 20.0),
            Pair(LocalDate.now().minusDays(2), 50.0),
            Pair(LocalDate.now().minusDays(1), 40.0),
            Pair(LocalDate.now(), 35.0)
        )
    }

    fun getWorkoutHistory(): List<WorkoutHistory> {
        return listOf(
            WorkoutHistory(name = "Legs", date = LocalDate.now().minusDays(5)),
            WorkoutHistory(name = "Back", date = LocalDate.now().minusDays(2)),
            WorkoutHistory(name = "Bench", date = LocalDate.now())
        )
    }

    fun getWorkoutAttendance(): Double {
        return 3.2
    }

    fun getSelectedExercise(): String {
        return "Lateral Raises"
    }

    fun getExercises(): List<Exercise> {
        return listOf(
            Exercise(name = "Lateral Raises"),
            Exercise(name = "Bench Press"),
            Exercise(name = "Squat")
        )
    }

    fun getSelectedExerciseWeightData(): Map<LocalDate, List<Double>> {
        return mapOf(
            Pair(LocalDate.now().minusDays(1), listOf(25.0, 25.0, 20.0)),
            Pair(LocalDate.now(), listOf(25.0, 20.0, 20.0))
        )
    }

    fun getSelectedExerciseRepsData(): Map<LocalDate, List<Double>> {
        return mapOf(
            Pair(LocalDate.now().minusDays(1), listOf(10.0, 9.0, 8.0)),
            Pair(LocalDate.now(), listOf(10.0, 10.0, 9.0))

        )
    }
}