package com.healthtracking.app.services.nutritiondata

import com.healthtracking.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

/**
 * Class for handling the nutritional data API
 * e.g. fetching nutritional data for a given food item in a range of measurements
 */
class NutritionalDataAPI() {
    object RetroFitInstance {
        private const val BASE_URL = "https://api.calorieninjas.com"
        private const val NUTRITIONAL_API_KEY = BuildConfig.NUTRITIONAL_API_KEY

        private val apiKeyInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Api-Key", NUTRITIONAL_API_KEY)
                .build()
            chain.proceed(request)
        }

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()

        val retrofitService : NutritionalDataHandler by lazy {
            retrofit.create(NutritionalDataHandler::class.java)
        }
    }

    /**
     * Get the nutritional data for a given food item in its measurement
     */
    suspend fun getNutritionalData(foodItem: String, measurement: String): NutritionalData? {
        try {
            val query: String = when (measurement) {
                "1 unit" -> "1 $foodItem"
                else -> "$measurement of $foodItem"
            }
            val data = RetroFitInstance.retrofitService.getNutritionalDataForItem(query = query)

            val jsonObject = JSONObject(data)
            val itemsArray = jsonObject.getJSONArray("items")
            val item = itemsArray.getJSONObject(0)

            // Extract values for fat, protein, and carbohydrates
            val totalGrams = item.getDouble("serving_size_g")
            val proteinTotal = item.getDouble("protein_g")
            val carbsTotal = item.getDouble("carbohydrates_total_g")
            val fatTotal = item.getDouble("fat_total_g")

            return NutritionalData(
                grams = totalGrams.toFloat(),
                protein = proteinTotal.toFloat(),
                carbohydrates = carbsTotal.toFloat(),
                fats = fatTotal.toFloat()
            )
        } catch (e: IOException) {
            return null
        } catch (e: Exception) {
            return null
        }
    }
}