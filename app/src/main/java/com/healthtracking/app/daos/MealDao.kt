package com.healthtracking.app.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.Meal
import com.healthtracking.app.entities.MealFoodCrossRef
import java.time.LocalDateTime

/**
 * DAO for meal entities
 */
@Dao
interface MealDao {
    @Upsert
    suspend fun upsertMealEntity(mealEntity: Meal): Long

    @Query("SELECT * FROM MEAL WHERE id = :mealId")
    fun getMealEntry(mealId: Long): Meal?

    @Query("SELECT * FROM MEAL WHERE DATE(date) = DATE(:date) ORDER BY date DESC")
    fun getTodaysMealEntries(date: LocalDateTime): LiveData<List<Meal>?>

    @Upsert
    fun upsertMealFoodCrossRef(crossRef: MealFoodCrossRef)

    @Upsert
    fun upsertFoodEntity(foodEntity: Food): Long
}