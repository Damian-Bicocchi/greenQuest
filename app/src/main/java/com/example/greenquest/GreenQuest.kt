package com.example.greenquest

import android.app.Application
import androidx.room.Room
import com.example.greenquest.database.AppDatabase

class GreenQuestApp : Application() {

    companion object {
        lateinit var instance: GreenQuestApp
            private set
    }

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "usuarios-db"
        ).build()
    }
}

