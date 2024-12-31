package nz.ac.canterbury.seng303.healthtracking

import nz.ac.canterbury.seng303.healthtracking.daos.WorkoutDao
import nz.ac.canterbury.seng303.healthtracking.database.AppDatabase
import nz.ac.canterbury.seng303.healthtracking.viewmodels.database.WorkoutViewModel
import nz.ac.canterbury.seng303.healthtracking.viewmodels.screen.AddWorkoutViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataAccessModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().workoutDao() }

    viewModel { WorkoutViewModel(get<WorkoutDao>()) }
    viewModel { AddWorkoutViewModel(get<WorkoutViewModel>())}
}