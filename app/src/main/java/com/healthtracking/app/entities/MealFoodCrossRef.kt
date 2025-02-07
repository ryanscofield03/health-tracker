package com.healthtracking.app.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "meal_food_cross_ref",
    primaryKeys = ["mealId", "foodId"],
    foreignKeys = [
        ForeignKey(
            entity = Meal::class,
            parentColumns = ["id"],
            childColumns = ["mealId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Food::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MealFoodCrossRef(
    val mealId: Long,
    val foodId: Long
)