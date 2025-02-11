package com.healthtracking.app


import com.healthtracking.app.daos.ExerciseDao
import com.healthtracking.app.daos.ExerciseHistoryDao
import com.healthtracking.app.daos.MealDao
import com.healthtracking.app.daos.SleepDao
import com.healthtracking.app.daos.WorkoutBackupDao
import com.healthtracking.app.daos.WorkoutDao
import com.healthtracking.app.daos.WorkoutHistoryDao
import com.healthtracking.app.database.AppDatabase
import com.healthtracking.app.viewmodels.screen.SleepScreenViewModel
import com.healthtracking.app.viewmodels.database.ExerciseHistoryViewModel
import com.healthtracking.app.viewmodels.database.ExerciseViewModel
import com.healthtracking.app.viewmodels.database.MealViewModel
import com.healthtracking.app.viewmodels.database.SleepViewModel
import com.healthtracking.app.viewmodels.database.WorkoutBackupViewModel
import com.healthtracking.app.viewmodels.database.WorkoutHistoryViewModel
import com.healthtracking.app.viewmodels.database.WorkoutViewModel
import com.healthtracking.app.viewmodels.screen.AddWorkoutViewModel
import com.healthtracking.app.viewmodels.screen.BuildMealViewModel
import com.healthtracking.app.viewmodels.screen.FoodViewModel
import com.healthtracking.app.viewmodels.screen.RunWorkoutViewModel
import com.healthtracking.app.viewmodels.screen.SettingsViewModel
import com.healthtracking.app.viewmodels.screen.StatsScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataAccessModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().workoutDao() }
    single { get<AppDatabase>().exerciseDao() }
    single { get<AppDatabase>().exerciseHistoryDao() }
    single { get<AppDatabase>().workoutBackupDao() }
    single { get<AppDatabase>().sleepDao() }
    single { get<AppDatabase>().mealDao() }
    single { get<AppDatabase>().workoutHistoryDao()}

    viewModel { WorkoutViewModel(get<WorkoutDao>()) }
    viewModel { ExerciseViewModel(get<ExerciseDao>()) }
    viewModel { WorkoutHistoryViewModel(get<WorkoutHistoryDao>()) }
    viewModel { ExerciseHistoryViewModel(get<ExerciseHistoryDao>()) }
    viewModel { AddWorkoutViewModel(get<WorkoutViewModel>(), get<ExerciseViewModel>()) }
    viewModel { WorkoutBackupViewModel(get<WorkoutBackupDao>()) }
    viewModel { SettingsViewModel(get()) }

    viewModel { SleepViewModel(get<SleepDao>()) }
    viewModel { SleepScreenViewModel(get<SleepViewModel>()) }

    single { MealViewModel(get(), get<MealDao>()) }
    viewModel { FoodViewModel(get<MealViewModel>()) }

    viewModel { StatsScreenViewModel(
        get<ExerciseViewModel>(),
        get<WorkoutHistoryViewModel>(),
        get<ExerciseHistoryViewModel>(),
        get<MealViewModel>(),
        get<SleepViewModel>()
    ) }

    viewModel { BuildMealViewModel(mealDao = get<MealDao>()) }

    viewModel { parameters -> RunWorkoutViewModel(
        savedStateHandle = get(),
        workout = parameters[0],
        exercises = parameters[1],
        exerciseHistoryViewModel = get(),
        workoutBackupViewModel = get(),
        workoutHistoryViewModel = get()
    ) }
}