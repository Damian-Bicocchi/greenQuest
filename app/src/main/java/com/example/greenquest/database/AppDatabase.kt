package com.example.greenquest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.greenquest.converters.Converters
import com.example.greenquest.dao.HistorialResiduoDao
import com.example.greenquest.dao.ImageReportDao
import com.example.greenquest.database.user.User
import com.example.greenquest.dao.TriviaDao
import com.example.greenquest.dao.UserDao
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.database.reporte.ReporteData
import com.example.greenquest.database.trivia.OpcionesTrivia
import com.example.greenquest.database.trivia.PreguntaTrivia
import com.example.greenquest.database.trivia.RespuestaUsuario

@Database(
    entities = [
        User::class,
        PreguntaTrivia::class,
        OpcionesTrivia::class,
        RespuestaUsuario::class,
        HistorialResiduo::class,
        ReporteData::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun triviaDao(): TriviaDao

    abstract fun historialResiduoDao(): HistorialResiduoDao

    abstract fun imageDao(): ImageReportDao
}