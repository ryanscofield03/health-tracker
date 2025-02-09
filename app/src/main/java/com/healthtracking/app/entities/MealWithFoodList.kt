package com.healthtracking.app.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MealWithFoodList(
    @Embedded
    val meal: Meal,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MealFoodCrossRef::class,
            parentColumn = "mealId",
            entityColumn = "foodId"
        )
    )
    val foodItems: List<Food>
)