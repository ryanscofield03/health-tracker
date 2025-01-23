package com.healthtracking.app.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for meals which stores necessary fields (name and list of food)
 */
@Entity(
    tableName = "meal"
)
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val foodItems: List<Food>
)