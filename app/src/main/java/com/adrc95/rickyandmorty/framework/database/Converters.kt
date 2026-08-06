package com.adrc95.rickyandmorty.framework.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromIntList(value: List<Int>): String = value.joinToString(",")

    @TypeConverter
    fun toIntList(value: String): List<Int> = if (value.isEmpty()) {
        emptyList()
    } else {
        value.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toStringList(value: String): List<String> = if (value.isEmpty()) {
        emptyList()
    } else {
        value.split(",")
    }
}