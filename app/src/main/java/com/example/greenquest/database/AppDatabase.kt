package com.example.greenquest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.greenquest.User
import com.example.greenquest.Usuario
import com.example.greenquest.dao.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}