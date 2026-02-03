package com.example.greenquest.database.converter

import androidx.room.TypeConverter

class ConverterList {
    @TypeConverter
    fun fromListIntToString(value: MutableList<Int>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toListIntFromString(stringList: String): MutableList<Int> {
        val result = ArrayList<Int>()
        val split =stringList.replace("[","").replace("]","").replace(" ","").split(",")
        for (n in split) {
            try {
                result.add(n.toInt())
            } catch (e: Exception) {

            }
        }
        return result
    }
}