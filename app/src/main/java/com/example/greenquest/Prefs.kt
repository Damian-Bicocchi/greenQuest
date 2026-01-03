package com.example.greenquest

import android.content.Context

class Prefs(val context: Context) {

    val SHARED_NAME = "databaseTrivia"
    val SHARED_TRIVIA_VERSION = "triviaVersion"
    val storage = context.getSharedPreferences(SHARED_NAME, 0)


    fun saveTriviaVersion(version: Int){
        storage.edit().putInt(SHARED_TRIVIA_VERSION, version).apply()
    }

    fun getTriviaVersion(): Int{
        return storage.getInt(SHARED_TRIVIA_VERSION, 0)
    }
}