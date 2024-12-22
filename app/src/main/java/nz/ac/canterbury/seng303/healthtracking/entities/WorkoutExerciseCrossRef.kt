package nz.ac.canterbury.seng303.healthtracking.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["workoutId", "exerciseId"],
    indices = [Index("workoutId"), Index("exerciseId")]
)
data class WorkoutExerciseCrossRef(
    val workoutId: Long,
    val exerciseId: Long
)