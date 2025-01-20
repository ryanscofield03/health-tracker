package com.healthtracking.app.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.healthtracking.app.entities.WorkoutBackup

@Dao
interface WorkoutBackupDao {
    @Upsert
    suspend fun upsertWorkoutBackup(workoutBackup: WorkoutBackup): Long

    @Query("SELECT * FROM workout_backup WHERE id = :workoutId")
    fun getWorkoutBackup(workoutId: Long): WorkoutBackup?

    @Query("DELETE FROM workout_backup WHERE id = :workoutId")
    fun deleteWorkoutBackup(workoutId: Long)
}