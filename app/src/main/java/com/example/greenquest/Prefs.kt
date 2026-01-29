package com.example.greenquest

import android.content.Context
import androidx.core.content.edit

class Prefs(appContext: Context) {
    companion object {
        const val SHARED_NAME = "databaseTrivia"
        const val SHARED_TRIVIA_VERSION = "triviaVersion"
    }

    private val storage = appContext.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)


    fun saveTriviaVersion(version: Int){
        storage.edit { putInt(SHARED_TRIVIA_VERSION, version) }
    }

    fun getTriviaVersion(): Int{
        return storage.getInt(SHARED_TRIVIA_VERSION, 0)
    }
}