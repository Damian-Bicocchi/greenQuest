package com.example.greenquest

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.migration.Migration
import com.example.greenquest.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class GreenQuestApp : Application() {



    companion object {
        lateinit var instance: GreenQuestApp
            private set
    }

    lateinit var database: AppDatabase
        private set

    lateinit var tokenManager: TokenManager
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        TokenDataStoreProvider.init(this)


        // 3️⃣ Inicializar Room
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "usuarios-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}

