package com.healthtracking.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.healthtracking.app.daos.ExerciseDao
import com.healthtracking.app.daos.ExerciseHistoryDao
import com.healthtracking.app.daos.MealDao
import com.healthtracking.app.daos.SleepDao
import com.healthtracking.app.daos.WorkoutBackupDao
import com.healthtracking.app.daos.WorkoutDao
import com.healthtracking.app.database.converters.Converters
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.ExerciseHistory
import com.healthtracking.app.entities.ExerciseHistoryCrossRef
import com.healthtracking.app.entities.Meal
import com.healthtracking.app.entities.Sleep
import com.healthtracking.app.entities.WorkoutBackup
import com.healthtracking.app.entities.Workout
import com.healthtracking.app.entities.WorkoutExerciseCrossRef

/**
 * Class for instantiating and getting the app's database as singleton
 */
@Database(entities = [
                        Workout::class,
                        Exercise::class,
                        WorkoutExerciseCrossRef::class,
                        ExerciseHistory::class,
                        ExerciseHistoryCrossRef::class,
                        WorkoutBackup::class,
                        Sleep::class,
                        Meal::class
                     ], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun exerciseHistoryDao(): ExerciseHistoryDao
    abstract fun workoutBackupDao(): WorkoutBackupDao

    abstract fun sleepDao(): SleepDao

    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}