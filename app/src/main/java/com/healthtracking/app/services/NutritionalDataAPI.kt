package com.healthtracking.app.services

import com.healthtracking.app.BuildConfig
import com.healthtracking.app.entities.Food

/**
 * Class for handling the nutritional data API
 * e.g. fetching nutritional data for a given food item in a range of measurements
 */
class NutritionalDataAPI {
    val nutritionalApiKey = BuildConfig.NUTRITIONAL_API_KEY

    /**
     * Retrieve a list of nutritional data for a given food item in different measurements
     * that make sense for the food item (e.g. egg should have 1 tbsp as an option)
     */
    private fun getNutritionalDataList(searchQuery: String): List<Food> {
        // actual implementation should try and match searchQuery to an actual item (e.g. brea -> bread)

        // actual implementation should have some debouncing to prevent too many requests

        return listOf(
            getNutritionalData(foodItem = searchQuery, measurement = "100g"),
            getNutritionalData(foodItem = searchQuery, measurement = "1 Unit"), // something like 1 egg or 1 chicken breast
            getNutritionalData(foodItem = searchQuery, measurement = "1 Cup"),
            getNutritionalData(foodItem = searchQuery, measurement = "1 Tbsp"),
        )
    }

    /**
     * Get the nutritional data for a given food item in its measurement
     */
    private fun getNutritionalData(foodItem: String, measurement: String): Food {
        return Food(
            name = "$foodItem + ($measurement)",
            calories = 100f,
            protein = 10f,
            carbohydrates = 10f,
            fats = 10f
        )
    }
}