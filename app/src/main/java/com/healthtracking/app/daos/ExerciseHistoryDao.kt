package com.healthtracking.app.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.healthtracking.app.entities.ExerciseHistory
import com.healthtracking.app.entities.ExerciseHistoryCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseHistoryDao {
    @Upsert
    suspend fun upsertWorkoutHistory(exerciseHistory: ExerciseHistory): Long

    @Upsert
    suspend fun upsertWorkoutHistoryCrossRef(exerciseHistoryCrossRef: ExerciseHistoryCrossRef)

    @Query("""
        SELECT eh.* 
        FROM exercise_history eh
        INNER JOIN exercise_history_cross_ref ehcr ON eh.id = ehcr.exerciseHistoryId
        WHERE ehcr.exerciseId = :exerciseId
        ORDER BY date DESC
        LIMIT 1
    """)
    fun getMostRecentHistoryForExercise(exerciseId: Long): ExerciseHistory?

    @Query("""
        SELECT eh.* 
        FROM exercise_history eh
        INNER JOIN exercise_history_cross_ref ehcr ON eh.id = ehcr.exerciseHistoryId
        WHERE ehcr.exerciseId = :exerciseId
    """)
    fun getHistoryForExercise(exerciseId: Long): List<ExerciseHistory>

    @Query("""
        SELECT eh.* 
        FROM exercise_history eh
        INNER JOIN exercise_history_cross_ref ehcr ON eh.id = ehcr.exerciseHistoryId
        INNER JOIN exercise e ON ehcr.exerciseId = e.id
        WHERE e.name = :exerciseName
    """)
    fun getHistoryForExerciseName(exerciseName: String): Flow<List<ExerciseHistory>?>
}