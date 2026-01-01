package com.example.greenquest

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.greenquest.database.AppDatabase


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class GreenQuestApp : Application() {
    companion object {
        lateinit var instance: GreenQuestApp
            private set
        lateinit var prefs: Prefs
    }

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        TokenDataStoreProvider.init(this)

        database = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "usuarios-db"
        ).fallbackToDestructiveMigration(false).build()

        prefs = Prefs(applicationContext)
    }

}

