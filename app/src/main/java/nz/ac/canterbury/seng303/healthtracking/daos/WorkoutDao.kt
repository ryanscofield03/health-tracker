package nz.ac.canterbury.seng303.healthtracking.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import nz.ac.canterbury.seng303.healthtracking.entities.Workout
import nz.ac.canterbury.seng303.healthtracking.entities.WorkoutExerciseCrossRef

/**
 * DAO for workouts, this allows CRUD operations on workouts in DB
 */
@Dao
interface WorkoutDao {
    @Upsert
    suspend fun upsertWorkout(workout: Workout)

    @Upsert
    suspend fun upsertWorkoutExerciseCrossRef(crossRef: WorkoutExerciseCrossRef)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("DELETE FROM WorkoutExerciseCrossRef WHERE workoutId = :workoutId AND exerciseId = :exerciseId")
    suspend fun deleteWorkoutExerciseCrossRef(workoutId: Long, exerciseId: Long)

    @Query("SELECT * FROM workout")
    fun getAllWorkouts(): LiveData<List<Workout>>
}