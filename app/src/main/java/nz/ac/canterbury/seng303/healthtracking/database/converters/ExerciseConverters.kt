package nz.ac.canterbury.seng303.healthtracking.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import nz.ac.canterbury.seng303.healthtracking.entities.Exercise

class ExerciseConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromExerciseList(value: List<Exercise>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toExerciseList(value: String?): List<Exercise>? {
        val type = object : TypeToken<List<Exercise>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromNestedListOfPairs(value: List<List<Pair<Int, Int>>>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toNestedListOfPairs(value: String?): List<List<Pair<Int, Int>>>? {
        val type = object : TypeToken<List<List<Pair<Int, Int>>>>() {}.type
        return gson.fromJson(value, type)
    }
}