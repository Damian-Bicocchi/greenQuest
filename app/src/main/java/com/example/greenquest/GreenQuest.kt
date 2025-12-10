package com.example.greenquest

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
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

        val MIGRATION1_2  = object : Migration(1,2){
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE User ADD COLUMN puntos INTEGER DEFAULT 0")
                database.execSQL("ALTER TABLE User ADD COLUMN imagen TEXT DEFAULT NULL")
            }
        }

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "usuarios-db"
        ).addMigrations(MIGRATION1_2)
            .build()
    }
}

