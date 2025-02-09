package com.healthtracking.app.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.healthtracking.app.entities.Food
import com.healthtracking.app.entities.Meal
import com.healthtracking.app.entities.MealFoodCrossRef
import com.healthtracking.app.entities.MealWithFoodList
import kotlinx.coroutines.flow.Flow
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

    @Query("""
        SELECT * 
        FROM MEAL 
        WHERE date >= :startOfDay AND date < :endOfDay
        ORDER BY date DESC
    """)
    fun getTodaysMealEntries(startOfDay: LocalDateTime, endOfDay: LocalDateTime): Flow<List<MealWithFoodList>?>

    @Upsert
    fun upsertMealFoodCrossRef(crossRef: MealFoodCrossRef)

    @Upsert
    fun upsertFoodEntity(foodEntity: Food): Long

    @Query("DELETE FROM MEAL WHERE id = :mealId")
    fun deleteMeal(mealId: Long): Int

    @Query("""
        SELECT * FROM meal 
    """)
    fun getAllMealEntries(): Flow<List<MealWithFoodList>?>
}