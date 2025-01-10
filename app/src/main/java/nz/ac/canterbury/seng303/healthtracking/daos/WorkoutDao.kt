package nz.ac.canterbury.seng303.healthtracking.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import io.reactivex.Flowable
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise
import nz.ac.canterbury.seng303.healthtracking.entities.Workout
import nz.ac.canterbury.seng303.healthtracking.entities.WorkoutExerciseCrossRef

/**
 * DAO for workouts, this allows CRUD operations on workouts in DB
 */
@Dao
interface WorkoutDao {
    @Upsert
    suspend fun upsertWorkout(workout: Workout): Long

    @Upsert
    suspend fun upsertWorkoutExerciseCrossRef(crossRef: WorkoutExerciseCrossRef)

    @Delete
    suspend fun deleteWorkoutAndExercises(workout: Workout, exercises: List<Exercise>)

    @Query("""
        DELETE 
        FROM WorkoutExerciseCrossRef 
        WHERE workoutId = :workoutId AND exerciseId = :exerciseId
        """)
    suspend fun deleteWorkoutExerciseCrossRef(workoutId: Long, exerciseId: Long)

    @Query("SELECT * FROM workout")
    fun getAllWorkouts(): LiveData<List<Workout>>

    @Query("""
        SELECT e.* 
        FROM Exercise e
        INNER JOIN WorkoutExerciseCrossRef wecr ON e.id = wecr.exerciseId
        WHERE wecr.workoutId = :workoutId
    """)
    fun getExercisesForWorkout(workoutId: Long): List<Exercise>
}