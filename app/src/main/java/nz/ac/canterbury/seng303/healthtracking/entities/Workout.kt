package nz.ac.canterbury.seng303.healthtracking.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

/**
 * Entity for workout which stores necessary fields
 */
@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var description: String,
    var schedule: List<DayOfWeek>
)
