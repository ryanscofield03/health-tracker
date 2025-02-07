package com.healthtracking.app.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "workout_backup"
)
data class WorkoutBackup(
    @PrimaryKey val id: Long,
    val exerciseIndex: Int,
    val entries: List<List<Pair<Float, Int>>>,
    val timerStart: LocalDateTime
)