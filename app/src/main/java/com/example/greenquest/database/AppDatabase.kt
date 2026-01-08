package com.example.greenquest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.greenquest.database.user.User
import com.example.greenquest.dao.TriviaDao
import com.example.greenquest.dao.UserDao
import com.example.greenquest.database.trivia.OpcionesTrivia
import com.example.greenquest.database.trivia.PreguntaConOpciones
import com.example.greenquest.database.trivia.PreguntaTrivia
import com.example.greenquest.database.trivia.RespuestaUsuario

@Database(
    entities = [
        User::class,
        PreguntaTrivia::class,
        OpcionesTrivia::class,
        RespuestaUsuario::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun triviaDao(): TriviaDao
}