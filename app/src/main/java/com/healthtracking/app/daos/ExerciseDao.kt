package com.healthtracking.app.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.WorkoutExerciseCrossRef

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

    @Query("SELECT * FROM exercise e WHERE e.name == :name")
    fun getExerciseWithName(name: String): Exercise?
}