package nz.ac.canterbury.seng303.healthtracking.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity for exercise which stores necessary fields
 */
@Entity(
    tableName = "exercise",
    indices = [Index(value = ["name"], unique = true)]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)