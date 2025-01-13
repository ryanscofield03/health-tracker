package nz.ac.canterbury.seng303.healthtracking.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.entities.WorkoutExerciseCrossRef

/**
 * DAO for interfacing with DB for exercise entities
 */
@Dao
interface ExerciseDao {
    @Upsert
    suspend fun upsertExercise(exercise: Exercise): Long

    @Upsert
    suspend fun upsertWorkoutExerciseCrossRef(workoutExerciseCrossRef: WorkoutExerciseCrossRef)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM workout")
    fun getAllExercises(): LiveData<List<Exercise>>
}