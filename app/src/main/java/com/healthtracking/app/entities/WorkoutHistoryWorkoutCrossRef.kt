package com.healthtracking.app.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
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
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val workoutId: Long,
    val workoutHistoryId: Long
)