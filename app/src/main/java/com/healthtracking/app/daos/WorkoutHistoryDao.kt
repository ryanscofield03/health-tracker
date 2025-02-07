package com.healthtracking.app.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.healthtracking.app.entities.WorkoutHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao {
    @Query("SELECT * FROM workout_history")
    fun getAllWorkoutHistory(): Flow<List<WorkoutHistory>?>

    @Upsert
    suspend fun upsertWorkoutHistory(workoutHistory: WorkoutHistory)
}