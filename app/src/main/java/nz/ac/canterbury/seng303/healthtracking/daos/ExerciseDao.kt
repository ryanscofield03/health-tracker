package nz.ac.canterbury.seng303.healthtracking.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise

/**
 * DAO for interfacing with DB for exercise entities
 */
@Dao
interface ExerciseDao {
    @Upsert
    suspend fun upsertExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM workout")
    fun getAllExercises(): LiveData<List<Exercise>>
}