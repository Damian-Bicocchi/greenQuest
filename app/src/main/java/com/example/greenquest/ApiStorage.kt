package com.example.greenquest

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ApiStorage(appContext: Context) {
    companion object {
        const val STORAGE_NAME = "apiStorage"
        const val API_URL = "apiURL"
    }

    private val storage = appContext.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)

    fun saveApiURL(url: String){
        storage.edit { putString(API_URL, url) }
    }

    fun getApiURL(): String {
        val url = storage.getString(API_URL, BuildConfig.BASE_URL)!!
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            saveApiURL(BuildConfig.BASE_URL)
            return BuildConfig.BASE_URL
        }
        return url
    }

    fun isDefault(): Boolean {
        return storage.getString(API_URL, BuildConfig.BASE_URL)!! == BuildConfig.BASE_URL
    }
}