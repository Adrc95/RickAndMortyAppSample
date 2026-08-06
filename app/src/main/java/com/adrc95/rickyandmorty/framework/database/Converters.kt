package com.adrc95.rickyandmorty.framework.database

import androidx.room.TypeConverter
import com.adrc95.rickyandmorty.framework.database.DatabaseConstants.COMMA_CONVERTERS_DELIMITER

class Converters {
    @TypeConverter
    fun fromIntList(value: List<Int>): String = value.joinToString(COMMA_CONVERTERS_DELIMITER)

    @TypeConverter
    fun toIntList(value: String): List<Int> = if (value.isEmpty()) {
        emptyList()
    } else {
        value.split(COMMA_CONVERTERS_DELIMITER).map { it.toInt() }
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(COMMA_CONVERTERS_DELIMITER)

    @TypeConverter
    fun toStringList(value: String): List<String> = if (value.isEmpty()) {
        emptyList()
    } else {
        value.split(COMMA_CONVERTERS_DELIMITER)
    }
}