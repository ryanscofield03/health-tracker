package nz.ac.canterbury.seng303.healthtracking.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "exercise_history"
)
data class ExerciseHistory (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val data: List<Pair<Int, Int>>
)