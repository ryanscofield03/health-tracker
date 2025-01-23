package com.healthtracking.app.entities

import androidx.room.Entity

/**
 * Entity for food which stores necessary fields (name, calories, and macros)
 */
@Entity(
    tableName = "food"
)
data class Food(
    val name: String,
    val calories: Float,
    val protein: Float,
    val fat: Float,
    val carbohydrates: Float
)