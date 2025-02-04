package com.healthtracking.app.services

import com.healthtracking.app.BuildConfig
import com.healthtracking.app.entities.Food

class NutritionalDataAPI {
    val nutritionalApiKey = BuildConfig.NUTRITIONAL_API_KEY

    private fun getNutritionalDataList(searchQuery: String): List<Food> {
        // actual implementation should try and match searchQuery to an actual item (e.g. brea -> bread)

        // actual implementation should have some debouncing to prevent too many requests

        return listOf(
            getNutritionalData("$searchQuery (100g)"),
            getNutritionalData("$searchQuery (1 cup)"),
            getNutritionalData("$searchQuery (1 tbsp)"),
            getNutritionalData("$searchQuery (1 tsp)")
            // extend to allow for something like 1 egg or 1 chicken breast (1 unit)
        )
    }

    private fun getNutritionalData(foodItem: String): Food {
        return Food(
            name = foodItem,
            calories = 100f,
            protein = 10f,
            carbohydrates = 10f,
            fats = 10f
        )
    }
}