package nz.ac.canterbury.seng303.healthtracking.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "workout_history"
)
data class WorkoutHistory (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val exercises: List<Exercise>,
    val entries: List<List<Pair<Int, Int>>>
)