package nz.ac.canterbury.seng303.healthtracking.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for workout which stores necessary fields
 */
@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
)
