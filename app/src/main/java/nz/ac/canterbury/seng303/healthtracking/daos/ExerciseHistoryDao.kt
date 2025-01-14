package nz.ac.canterbury.seng303.healthtracking.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import nz.ac.canterbury.seng303.healthtracking.entities.ExerciseHistory
import nz.ac.canterbury.seng303.healthtracking.entities.ExerciseHistoryCrossRef

@Dao
interface ExerciseHistoryDao {
    @Upsert
    suspend fun upsertWorkoutHistory(exerciseHistory: ExerciseHistory): Long

    @Upsert
    suspend fun upsertWorkoutHistoryCrossRef(exerciseHistoryCrossRef: ExerciseHistoryCrossRef)

    @Query("""
        SELECT eh.* 
        FROM exercise_history eh
        INNER JOIN ExerciseHistoryCrossRef ehcr ON eh.id = ehcr.exerciseHistoryId
        WHERE ehcr.exerciseId = :exerciseId
        ORDER BY date DESC
        LIMIT 1
    """)
    fun getMostRecentHistoryForExercise(exerciseId: Long): ExerciseHistory?

    @Query("""
        SELECT eh.* 
        FROM exercise_history eh
        INNER JOIN ExerciseHistoryCrossRef ehcr ON eh.id = ehcr.exerciseHistoryId
        WHERE ehcr.exerciseId = :exerciseId
    """)
    fun getHistoryForExercise(exerciseId: Long): List<ExerciseHistory>
}