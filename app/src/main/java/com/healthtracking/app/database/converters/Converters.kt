package com.healthtracking.app.database.converters

import androidx.room.TypeConverter
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
    fun fromDaysList(value: List<DayOfWeek>): String {
        return value.joinToString(separator = ",") { it.name }
    }

    @TypeConverter
    fun toDaysList(value: String): List<DayOfWeek> {
        return if (value.isBlank()) {
            emptyList()
        } else {
            value.split(",").map { DayOfWeek.valueOf(it) }
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
    fun fromListOfPairs(value: List<Pair<Int, Int>>): String {
        return value.joinToString(",") { "${it.first}:${it.second}" }
    }

    @TypeConverter
    fun toListOfPairs(value: String): List<Pair<Int, Int>> {
        if (value.isBlank()) {
            return emptyList()
        }

        return value.split(",")
            .map {
                val (first, second) = it.split(":")
                Pair(first.toInt(), second.toInt())
            }
    }

    @TypeConverter
    fun fromEntryBackupList(value: List<List<Pair<Int, Int>>>): String {
        return value.joinToString(";") { fromListOfPairs(it) }
    }

    @TypeConverter
    fun toEntryBackupList(value: String): List<List<Pair<Int, Int>>> {
        if (value.isBlank()) {
            return emptyList()
        }

        return value.split(";")
            .map { toListOfPairs(it) }
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
}