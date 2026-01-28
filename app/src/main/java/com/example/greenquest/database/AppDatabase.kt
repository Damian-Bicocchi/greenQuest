package com.example.greenquest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.greenquest.dao.TiendaDao
import com.example.greenquest.database.user.User
import com.example.greenquest.dao.TriviaDao
import com.example.greenquest.dao.UserDao
import com.example.greenquest.database.trivia.OpcionesTrivia
import com.example.greenquest.database.trivia.PreguntaConOpciones
import com.example.greenquest.database.trivia.PreguntaTrivia

@TypeConverters(com.example.greenquest.database.converter.ConverterList::class)
@Database(entities = [User::class, PreguntaTrivia::class, OpcionesTrivia::class, TiendaAdquiridos::class], version = 9, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun triviaDao(): TriviaDao

    abstract fun tiendaDao(): TiendaDao
}