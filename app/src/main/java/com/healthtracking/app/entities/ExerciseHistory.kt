package com.healthtracking.app.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "exercise_history"
)
data class ExerciseHistory (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDateTime,
    val data: List<Pair<Float, Int>>
)