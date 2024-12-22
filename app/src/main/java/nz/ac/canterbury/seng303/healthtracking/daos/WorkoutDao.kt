package nz.ac.canterbury.seng303.healthtracking.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import nz.ac.canterbury.seng303.healthtracking.entities.Workout

/**
 * DAO for workouts, this allows CRUD operations on workouts in DB
 */
@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workout: Workout)

    @Update
    suspend fun update(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("SELECT * FROM workout")
    fun getAllNotes(): LiveData<List<Workout>>
}