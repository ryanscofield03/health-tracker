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
    val measurement: String,
    val calories: Float,
    val protein: Float,
    val fats: Float,
    val carbohydrates: Float,
    val quantity: Float
)