package com.healthtracking.app.daos

import androidx.room.Dao
import androidx.room.Upsert
import com.healthtracking.app.entities.WorkoutHistory

@Dao
interface WorkoutHistoryDao {
    @Upsert
    suspend fun upsertWorkoutHistory(workoutHistory: WorkoutHistory)
}