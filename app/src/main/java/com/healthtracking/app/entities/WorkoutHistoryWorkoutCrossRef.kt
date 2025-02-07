package com.healthtracking.app.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "workout_history_workout_cross_ref",
    primaryKeys = ["workoutId", "workoutHistoryId"],
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WorkoutHistory::class,
            parentColumns = ["id"],
            childColumns = ["workoutHistoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutHistoryWorkoutCrossRef (
    val workoutId: Long,
    val workoutHistoryId: Long
)