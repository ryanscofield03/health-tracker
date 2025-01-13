package nz.ac.canterbury.seng303.healthtracking.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["exerciseId", "exerciseHistoryId"],
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseHistory::class,
            parentColumns = ["id"],
            childColumns = ["exerciseHistoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseHistoryCrossRef(
    val exerciseId: Long,
    val exerciseHistoryId: Long
)