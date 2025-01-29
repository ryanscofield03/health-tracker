package com.healthtracking.app.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workout_history")
data class WorkoutHistory (
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val name: String,
    val date: LocalDate
)