package com.healthtracking.app.services.nutritiondata

import retrofit2.http.GET
import retrofit2.http.Query

interface NutritionalDataHandler {
    @GET("/v1/nutrition")
    suspend fun getNutritionalDataForItem(
        @Query("query") query: String
    ): String
}
