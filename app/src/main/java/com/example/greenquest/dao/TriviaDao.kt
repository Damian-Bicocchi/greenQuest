package com.example.greenquest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.greenquest.database.trivia.OpcionesTrivia
import com.example.greenquest.database.trivia.PreguntaConOpciones
import com.example.greenquest.database.trivia.PreguntaTrivia

@Dao
interface TriviaDao {

    @Query("""
        SELECT * FROM preguntas 
        WHERE answered_question = 0
        ORDER BY RANDOM() 
        LIMIT 1
    """)
    suspend fun obtenerPreguntaAleatoria(): PreguntaTrivia?


    @Query("SELECT * FROM opciones WHERE preguntaCorrespondienteId = :preguntaId")
    suspend fun obtenerOpcionesPorPreguntaId(preguntaId: Long): List<OpcionesTrivia>


    @Transaction
    suspend fun obtenerPreguntaConOpcionesCompleta(): PreguntaConOpciones? {
        val pregunta = obtenerPreguntaAleatoria() ?: return null

        val opciones = obtenerOpcionesPorPreguntaId(pregunta.preguntaId)
        return PreguntaConOpciones(pregunta, opciones)
    }

    @Transaction
    @Query("SELECT * FROM preguntas WHERE preguntaId = :id")
    suspend fun obtenerPreguntaConOpcionesPorId(id: Long): PreguntaConOpciones?

    @Transaction
    @Update
    suspend fun actualizarPregunta(pregunta: PreguntaTrivia) : Int

    @Transaction
    @Insert
    suspend fun insertarPregunta(pregunta: PreguntaTrivia): Long

    @Transaction
    @Insert
    suspend fun insertarOpciones(opciones: List<OpcionesTrivia>)

    @Query("SELECT COUNT(*) FROM opciones WHERE preguntaCorrespondienteId = :preguntaId")
    suspend fun contarOpciones(preguntaId: Long): Int



}