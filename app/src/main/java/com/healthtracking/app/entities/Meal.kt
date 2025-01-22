package com.healthtracking.app.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for food/meals which stores necessary fields
 */
@Entity(
    tableName = "meal",
)
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val foodItems: List<Food>
)

data class Food(
    val name: String,
    val calories: Float,
    val protein: Float,
    val fat: Float,
    val carbohydrates: Float
)