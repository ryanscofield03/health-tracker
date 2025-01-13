package nz.ac.canterbury.seng303.healthtracking.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nz.ac.canterbury.seng303.healthtracking.daos.ExerciseDao
import nz.ac.canterbury.seng303.healthtracking.daos.WorkoutDao
import nz.ac.canterbury.seng303.healthtracking.daos.WorkoutHistoryDao
import nz.ac.canterbury.seng303.healthtracking.database.converters.Converters
import nz.ac.canterbury.seng303.healthtracking.database.converters.ExerciseConverters
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.entities.Workout
import nz.ac.canterbury.seng303.healthtracking.entities.WorkoutExerciseCrossRef
import nz.ac.canterbury.seng303.healthtracking.entities.WorkoutHistory
import nz.ac.canterbury.seng303.healthtracking.entities.WorkoutHistoryCrossRef

/**
 * Class for instantiating and getting the app's database as singleton
 */
@Database(entities = [
    Workout::class,
    Exercise::class,
    WorkoutExerciseCrossRef::class,
    WorkoutHistory::class,
    WorkoutHistoryCrossRef::class], version = 1)
@TypeConverters(*[Converters::class, ExerciseConverters::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao

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