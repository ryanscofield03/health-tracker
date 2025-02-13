package com.healthtracking.app.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Entity for workout which stores necessary fields
 */
@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var name: String,
    var description: String,

    /**
     * represents day of week, and true if notification has already been sent out
     */
    var schedule: List<Pair<DayOfWeek, LocalDate>>
)
