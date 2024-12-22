package nz.ac.canterbury.seng303.healthtracking.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for exercise which stores necessary fields
 */
@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)
