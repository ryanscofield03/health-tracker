package nz.ac.canterbury.seng303.healthtracking.database

import androidx.room.TypeConverter
import java.time.DayOfWeek

/**
 * Used to convert data types that are not handled by Room into strings for Room to write into DB
 * And then back into their datatype when read from Room DB
 */
class Converters {
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }

    @TypeConverter
    fun fromFloatList(value: List<Float>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toFloatList(value: String?): List<Float>? {
        return value?.split(",")?.map { it.toFloat() }
    }

    @TypeConverter
    fun fromDaysList(value: List<DayOfWeek>): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun toDaysList(value: String): List<DayOfWeek> {
        return value.split(",").map { DayOfWeek.of(it.toInt()) }
    }
}