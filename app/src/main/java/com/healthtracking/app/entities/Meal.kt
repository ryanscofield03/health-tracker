package com.healthtracking.app.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Entity for meals which stores necessary fields (name and date) - actual food is crossref'd
 */
@Entity(
    tableName = "meal"
)
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val date: LocalDateTime
)