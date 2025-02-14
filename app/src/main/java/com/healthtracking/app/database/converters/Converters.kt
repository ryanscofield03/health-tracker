package com.healthtracking.app.database.converters

import androidx.room.TypeConverter
import com.healthtracking.app.entities.Metric
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

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
    fun fromDaysList(value: List<Pair<DayOfWeek, LocalDate>>): String {
        println(value)

        return value.joinToString(separator = ",") { "${it.first}:${fromLocalDate(it.second)}" }
    }

    @TypeConverter
    fun toDaysList(value: String): List<Pair<DayOfWeek, LocalDate>> {
        println(value)

        if (value.isBlank()) {
            return emptyList()
        }

        return value.split(",")
            .map {
                val (first, second) = it.split(":")
                Pair(DayOfWeek.valueOf(first), toLocalDate(second))
            }
    }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        return value?.atZone(ZoneId.systemDefault())?.toEpochSecond()
    }

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }


    @TypeConverter
    fun fromListOfFloatIntPair(value: List<Pair<Float, Int>>): String {
        return value.joinToString(",") { "${it.first}:${it.second}" }
    }

    @TypeConverter
    fun toListOfFloatIntPair(value: String): List<Pair<Float, Int>> {
        if (value.isBlank()) {
            return emptyList()
        }

        return value.split(",")
            .map {
                val (first, second) = it.split(":")
                Pair(first.toFloat(), second.toInt())
            }
    }

    @TypeConverter
    fun fromEntryBackupList(value: List<List<Pair<Float, Int>>>): String {
        return value.joinToString(";") { fromListOfFloatIntPair(it) }
    }

    @TypeConverter
    fun toEntryBackupList(value: String): List<List<Pair<Float, Int>>> {
        if (value.isBlank()) {
            return emptyList()
        }

        return value.split(";")
            .map { toListOfFloatIntPair(it) }
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate): String {
        return value.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    @TypeConverter
    fun fromLocalTime(value: LocalTime): String {
        return value.toString()
    }

    @TypeConverter
    fun toLocalTime(value: String): LocalTime {
        return LocalTime.parse(value)
    }

    @TypeConverter
    fun fromListOfMetrics(value: List<Metric>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toListOfMetrics(value: String): List<Metric> {
        return value.split(",").map { Metric.valueOf(it) }
    }
}