package com.healthtracking.app.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.entities.Workout

/**
 * DAO for workouts, this allows CRUD operations on workouts in DB
 */
@Dao
interface WorkoutDao {
    @Upsert
    suspend fun upsertWorkout(workout: Workout): Long

    @Transaction
    suspend fun deleteWorkoutAndAssociatedData(workout: Workout) {
        deleteWorkoutExerciseCrossRefs(workout.id)
        deleteAssociatedExercises(workout.id)
        deleteWorkout(workout.id)
    }

    @Query("DELETE FROM Workout WHERE id = :workoutId")
    suspend fun deleteWorkout(workoutId: Long)

    @Query("DELETE FROM workout_exercise_cross_ref WHERE workoutId = :workoutId")
    suspend fun deleteWorkoutExerciseCrossRefs(workoutId: Long)

    @Query("""
        DELETE FROM Exercise 
        WHERE id IN (
            SELECT e.id
            FROM Exercise e
            INNER JOIN workout_exercise_cross_ref wecr ON e.id = wecr.exerciseId
            WHERE wecr.workoutId = :workoutId
        )
    """)
    suspend fun deleteAssociatedExercises(workoutId: Long)

    @Query("""
        DELETE 
        FROM workout_exercise_cross_ref 
        WHERE workoutId = :workoutId AND exerciseId = :exerciseId
        """)
    suspend fun deleteWorkoutExerciseCrossRef(workoutId: Long, exerciseId: Long)

    @Query("SELECT * FROM workout")
    fun getAllWorkouts(): LiveData<List<Workout>>

    @Query("""
        SELECT e.* 
        FROM Exercise e
        INNER JOIN workout_exercise_cross_ref wecr ON e.id = wecr.exerciseId
        WHERE wecr.workoutId = :workoutId
    """)
    fun getExercisesForWorkout(workoutId: Long): List<Exercise>
}