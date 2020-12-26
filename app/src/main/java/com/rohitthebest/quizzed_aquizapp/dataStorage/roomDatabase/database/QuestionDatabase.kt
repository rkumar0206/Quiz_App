package com.rohitthebest.quizzed_aquizapp.dataStorage.roomDatabase.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rohitthebest.quizzed_aquizapp.dataStorage.roomDatabase.dao.QuestionDao
import com.rohitthebest.quizzed_aquizapp.remote.model.Result

@Database(entities = [Result::class], version = 1)
@TypeConverters(Converters::class)
abstract class QuestionDatabase : RoomDatabase() {

    abstract fun getQuestionDao(): QuestionDao
}

class Converters {

    val gson = Gson()

    @TypeConverter
    fun convertListToString(list: List<String>): String = gson.toJson(list)

    @TypeConverter
    fun convertStringToList(string: String): List<String> {

        val type = object : TypeToken<List<String>>() {}.type

        return gson.fromJson(string, type)
    }
}