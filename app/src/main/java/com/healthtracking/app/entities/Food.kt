package com.healthtracking.app.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for food which stores necessary fields (name, calories, and macros)
 */
@Entity(
    tableName = "food"
)
data class Food(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    val name: String,
    val measurement: String,
    val quantity: Float,
    val calories: Float,
    val protein: Float,
    val fats: Float,
    val carbohydrates: Float
)