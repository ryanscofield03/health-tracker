package nz.ac.canterbury.seng303.healthtracking.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import nz.ac.canterbury.seng303.healthtracking.entities.WorkoutHistory
import nz.ac.canterbury.seng303.healthtracking.entities.WorkoutHistoryCrossRef

@Dao
interface WorkoutHistoryDao {
    @Upsert
    suspend fun upsertWorkoutHistory(workoutHistory: WorkoutHistory): Long

    @Upsert
    suspend fun upsertWorkoutHistoryCrossRef(workoutHistoryCrossRef: WorkoutHistoryCrossRef)

    @Query("")
    suspend fun getMostRecentHistoryForWorkout(workoutId: Long)
}